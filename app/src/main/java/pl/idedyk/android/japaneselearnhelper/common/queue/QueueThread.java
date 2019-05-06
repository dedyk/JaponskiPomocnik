package pl.idedyk.android.japaneselearnhelper.common.queue;

import android.util.Log;

public class QueueThread extends Thread {

    private boolean stop = false;

    public void requestStop() {
        this.stop = true;

        Log.d("JapaneseAndroidLearnHel", "requestStop");
    }

    @Override
    public void run() {

        while(true) {

            if (stop == true) {
                break;
            }

            Log.d("JapaneseAndroidLearnHel", "Processing queue items");

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // noop
            }
        }
    }

    public void logScreen(String screenName) {
        Log.d("JapaneseAndroidLearnHel", "!!!!!!!!!!!!: logScreen: " + screenName);
    }

    public void logEvent(String screenName, String actionName, String label) {
        Log.d("JapaneseAndroidLearnHel", "!!!!!!!!!!!!: logEvent: " + screenName + " - " + actionName + " - " + label);
    }
}
