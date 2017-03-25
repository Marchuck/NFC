package pl.marchuck.nfc;

import android.app.Application;
import android.content.Context;

/**
 * Project "NfcUtils"
 * <p>
 * Created by Lukasz Marczak
 * on 25.03.2017.
 */

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }
}
