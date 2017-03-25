package pl.marchuck.nfc.write;

import android.app.Activity;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

interface NfcWriteView<T extends Activity> {

    T self();
}
