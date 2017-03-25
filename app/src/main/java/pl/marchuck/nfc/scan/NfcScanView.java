package pl.marchuck.nfc.scan;

import android.app.Activity;

import rx.Observable;

/**
 * Project "NfcUtils"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public interface NfcScanView<T extends Activity> {
    Observable<Boolean> ensurePermissions();

    void onNfcReady();

    void onTagRead(String tagValue);

    void onErrorReadTag();

    T self();
}
