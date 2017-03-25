package pl.marchuck.nfc;

import android.app.Application;
import android.content.Context;

import pl.marchuck.nfc.utils.SoundUtils;

/**
 * Project "NfcUtils"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public class App extends Application {

    public SoundUtils sounds;

    public static SoundUtils getSounds(Context context) {
        return ((App) context.getApplicationContext()).sounds;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sounds = new SoundUtils().prepare(this);

    }
}
