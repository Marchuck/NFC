package pl.marchuck.nfc.repository;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public class NfcDiscoveredNdefEvent extends NfcEvent {

    public static final String TAG = NfcDiscoveredNdefEvent.class.getSimpleName();

    Tag tag;

    public boolean hasTag() {
        return tag != null;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public void browseTag(Intent intent) {
        printData(intent);

//        String type = intent.getType();
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

//        if (NfcScanPresenter.MIME_TEXT_PLAIN.equals(type)) {
//
//            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        } else {
//            Log.d(TAG, "Wrong mime type: " + type);
//        }
    }

}
