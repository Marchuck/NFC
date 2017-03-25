package pl.marchuck.nfc.repository;

import android.content.Intent;
import android.nfc.NfcAdapter;

/**
 * Project "NFC"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public class NfcDiscoveredTechEvent extends NfcEvent {

    public static final String TAG = NfcDiscoveredTechEvent.class.getSimpleName();

    @Override
    public void browseTag(Intent intent) {
        printData(intent);
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        String[] techList = tag.getTechList();
//        String searchedTech = Ndef.class.getName();
//
//        for (String tech : techList) {
//            if (searchedTech.equals(tech)) {
//
//            }
//        }

    }
}
