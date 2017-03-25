package pl.marchuck.nfc.scan;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import pl.marchuck.nfc.R;
import rx.Observable;
import rx.functions.Func1;

public class NfcScanActivity extends AppCompatActivity implements NfcScanView<NfcScanActivity> {

    public static final String TAG = NfcScanActivity.class.getSimpleName();

    NfcScanPresenter presenter;
    PowerManager.WakeLock wakeLock;
    RxPermissions permissions;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        textView = (TextView) findViewById(R.id.text_nfc);

        View backBtn = findViewById(R.id.scan_back_button);
        if (backBtn != null) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        Log.d(TAG, "onCreate: ");
        permissions = new RxPermissions(this);
        presenter = new NfcScanPresenter<>(this);
        presenter.requestPermissions(this);
    }

    @Override
    public Observable<Boolean> ensurePermissions() {
        return permissions.request(Manifest.permission.WAKE_LOCK,
                Manifest.permission.NFC)
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public Observable<Boolean> call(Boolean granted) {
                        if (granted && Build.VERSION_CODES.KITKAT >= Build.VERSION.SDK_INT) {
                            return permissions.request(Manifest.permission.BIND_NFC_SERVICE);
                        } else {
                            return Observable.just(granted);
                        }
                    }
                });
    }

    @Override
    @SuppressWarnings("deprecated")
    public void onNfcReady() {
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(6, "My Tag");
        this.wakeLock.acquire();
        //presenter.handleIntent(getIntent());
    }

    @Override
    public void onTagRead(final String tagValue) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder(textView.getText());
                textView.setText(sb.append('\n').append(tagValue));
            }
        });
    }

    @Override
    public void onErrorReadTag() {
        Toast.makeText(this.getApplicationContext(), "Error read tag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public NfcScanActivity self() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        presenter.startNfc();
    }

    @Override
    protected void onPause() {
        presenter.pauseNfc();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        presenter.handleIntent(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        this.wakeLock.release();
        presenter .destroy();
        presenter  = null;
        super.onDestroy();
    }


}

