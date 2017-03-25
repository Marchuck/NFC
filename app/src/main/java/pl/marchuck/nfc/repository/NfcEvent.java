package pl.marchuck.nfc.repository;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */
public abstract class NfcEvent {

    public static final String TAG = NfcEvent.class.getSimpleName();

    public abstract void browseTag(Intent intent);

    protected Tag tag = null;

    public boolean hasTag() {
        return tag != null;
    }

    public Tag getTag() {
        return tag;
    }

    public static NfcEvent create(String action) {

        if (action == null) throw new IllegalStateException("action cannot be null!");

        switch (action) {
            case NfcAdapter.ACTION_NDEF_DISCOVERED: {
                return new NfcDiscoveredNdefEvent();
            }
            case NfcAdapter.ACTION_TECH_DISCOVERED: {
                return new NfcDiscoveredTechEvent();
            }
        }
        return Empty.INSTANCE;
    }

    static void printData(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                Log.d(TAG, "raw message: " + msgs[i]);
            }
        }
    }

    public static class Empty extends NfcEvent {
        public static Empty INSTANCE = new Empty();

        @Override
        public void browseTag(Intent intent) {
            Log.e(TAG, "browseTag: unsupported action " + intent.getAction());
        }
    }
}
