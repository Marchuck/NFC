package pl.marchuck.nfc;

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
public class NFC {
    private Context context;
    private NfcAdapter nfcAdapter;

    public NfcAdapter get() {
        return nfcAdapter;
    }

    public NFC(Context ctx) {
        this.context = ctx.getApplicationContext();
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    rx.Observable<String> getNfcMessage(final Tag tag) {
        return rx.Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    // NDEF is not supported by this Tag.
                    subscriber.onError(new NullPointerException("Tag not supported!"));
                    return;
                }
                NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                        try {
                            subscriber.onNext(readText(ndefRecord));
                        } catch (UnsupportedEncodingException e) {
                            //unsupported encoding exception
                            //subscriber.onError(e);
                        }
                    }
                }
                subscriber.onCompleted();
            }
        });
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
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
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

}
