package pl.marchuck.nfc.utils;

import rx.Subscriber;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public abstract class NfcObserver<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }
}
