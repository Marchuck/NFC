package pl.marchuck.nfc.utils;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 22.06.16.
 */
public class NfcUtils {


    public static final String TAG = NfcUtils.class.getSimpleName();
    public static final String UNKNOWN_STRING = "???";

    NfcAdapter nfcAdapter;

    public NfcAdapter getAdapter() {
        return nfcAdapter;
    }

    public NfcUtils(Context ctx) {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(ctx);
    }

    public rx.Observable<NdefRecord> readNfcTag(final Tag tag) {
        return rx.Observable.create(new Observable.OnSubscribe<NdefRecord>() {
            @Override
            public void call(Subscriber<? super NdefRecord> subscriber) {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    // NDEF is not supported by this Tag.
                    subscriber.onError(new NullPointerException("Tag not supported!"));
                    return;
                }

                NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                NdefRecord[] records = ndefMessage.getRecords();

                for (NdefRecord ndefRecord : records) {
                    Log.d(TAG, "call: " + ndefRecord.toMimeType());
                    if (isRecordValid(ndefRecord)) {
                        subscriber.onNext(ndefRecord);
                    }
                }
                subscriber.onCompleted();
            }

            private boolean isRecordValid(NdefRecord record) {
                return record.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                        Arrays.equals(record.getType(), NdefRecord.RTD_TEXT);
            }
        });
    }

    public static String readText(NdefRecord record) {
        /*
         * See NfcUtils forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        try {
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            return UNKNOWN_STRING;
        }
    }

}
