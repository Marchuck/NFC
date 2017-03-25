package pl.marchuck.nfc.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import pl.marchuck.nfc.App;
import pl.marchuck.nfc.R;

/**
 * @author Lukasz Marczak
 * @since 22.06.16.
 */
public class SoundUtils implements MediaPlayer.OnPreparedListener {
    private static final String TAG = SoundUtils.class.getSimpleName();

    private AtomicBoolean prepared = new AtomicBoolean(false);
    private MediaPlayer mediaPlayer;

    public SoundUtils prepare(Context c) {

        mediaPlayer = MediaPlayer.create(c.getApplicationContext(), R.raw.win95);
        mediaPlayer.setOnPreparedListener(this);
        return this;
    }

    private volatile Runnable playRunnable = null;

    public void play(final boolean silent) {

        playRunnable = new Runnable() {
            @Override
            public void run() {
                float vol = prepareVolume(silent);
                mediaPlayer.setVolume(vol, vol);
                mediaPlayer.start();
            }
        };

        boolean canPlay = !mediaPlayer.isPlaying() && prepared.get();
        if (canPlay) {
            playRunnable.run();
        }
    }

    private float prepareVolume(boolean silent) {

        if (silent) return .5f;
        return 1;

    }

    public static SoundUtils get(Context c) {
        return App.getSounds(c.getApplicationContext());
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared: ");
        prepared.set(true);
        if (playRunnable != null) {
            playRunnable.run();
        }
    }
}
