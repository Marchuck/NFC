package pl.marchuck.nfc.scan;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;

import pl.marchuck.nfc.utils.NfcUtils;
import pl.marchuck.nfc.repository.NfcEvent;
import pl.marchuck.nfc.utils.NfcObserver;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Project "NfcUtils"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public class NfcScanPresenter<T extends Activity> {

    public static final String TAG = NfcScanPresenter.class.getSimpleName();
    public static final String MIME_TEXT_PLAIN = "text/plain";

    NfcScanView<T> view;

    NfcUtils nfc;


    public NfcScanPresenter(NfcScanView<T> view) {
        this.view = view;
    }

    public void startNfc() {
        Log.d(TAG, "setupForegroundDispatch: ");

        final Intent intent = new Intent(view.self(), this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(view.self(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        nfc.getAdapter().enableForegroundDispatch(view.self(), pendingIntent, filters, techList);
    }

    public void pauseNfc() {
        Log.d(TAG, "stopForegroundDispatch: ");
        nfc.getAdapter().disableForegroundDispatch(view.self());
    }

    public void requestPermissions(final Context c) {
        view.ensurePermissions()
                .subscribe(new NfcObserver<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean granted) {
                        Log.d(TAG, "onNext: " + granted);
                        if (granted) {
                            createNfcUtils(c);
                            view.onNfcReady();
                        }
                    }
                });
    }

    private void createNfcUtils(Context c) {
        nfc = new NfcUtils(c);
    }

    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "handleIntent: " + action);

        NfcEvent event = NfcEvent.create(action);

        event.browseTag(intent);

        if (event.hasTag()) {
            Tag tag = event.getTag();

            Subscription s = nfc.readNfcTag(tag)
                    .map(new Func1<NdefRecord, String>() {
                        @Override
                        public String call(NdefRecord ndefRecord) {
                            return NfcUtils.readText(ndefRecord);
                        }
                    }).compose(this.<String>applySchedulers())
                    .subscribe(new NfcObserver<String>() {
                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: ",e );
                            view.onErrorReadTag();
                        }

                        @Override
                        public void onNext(String s) {
                            Log.d(TAG, "onNext: " + s  );
                            view.onTagRead(s);
                        }
                    });
        } else {
            Log.e(TAG, "handleIntent: no tag here");
        }
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
