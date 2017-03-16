package br.ufpe.cin.pet.geoquest.Utils;

import android.os.Handler;

import com.dd.morphingbutton.IProgress;

import java.util.Random;

/**
 * Created by caesa on 15/03/2017.
 */
public class ProgressGenerator {
    public interface OnCompleteListener {

        void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final IProgress button, int duration, final int progress) {
        mProgress = 0;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 1;
                button.setProgress(mProgress);
                if (mProgress < progress) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, duration);
    }

    public void start(final IProgress button, final int progress) {
        start(button, 1000, progress);
    }

    private Random random = new Random();

    private int generateDelay() {
        return 0;
    }
}

