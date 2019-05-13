package pl.idedyk.android.japaneselearnhelper.common.queue;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.japanese.dictionary.api.android.queue.event.IQueueEvent;
import pl.idedyk.japanese.dictionary.api.android.queue.event.StatLogScreenEvent;
import pl.idedyk.japanese.dictionary.api.android.queue.event.WordDictionaryMissingWordEvent;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.japanese.dictionary.api.android.queue.factory.QueueEventFactory;

public class QueueEventThread extends Thread {

    private boolean stop = false;

    private PackageInfo packageInfo = null;

    public QueueEventThread(PackageManager packageManager, String packageName) {

        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            // noop
        }
    }

    public void requestStop() {
        this.stop = true;

        Log.d("JapaneseAndroidLearnHel", "requestStop");
    }

    @Override
    public void run() {

        QueueEventFactory queueEventFactory = new QueueEventFactory();

        int counter = 0;

        while(true) {

            if (stop == true || counter > 60) {
                break;
            }

            DataManager dataManager = JapaneseAndroidLearnHelperApplication.getInstance().getDataManager();

            int sleepMode = 0;

            if (dataManager != null) {

                List<IQueueEvent> queueEventList = dataManager.getQueueEventList(queueEventFactory);

                for (IQueueEvent queueEvent : queueEventList) {

                    Log.d("JapaneseAndroidLearn", "QueueEventThread - processing: " + queueEvent.getId() + " - " + queueEvent.getQueryEventOperation() + " - " + queueEvent.getParamsAsString());

                    boolean processed = false;

                    // przetwarzamy zdarzenie
                    switch (queueEvent.getQueryEventOperation()) {

                        case STAT_START_APP_EVENT:

                            processed = processStatStartAppEvent(queueEvent);

                            break;

                        case STAT_LOG_SCREEN_EVENT:

                            processed = processStatLogScreenEvent(queueEvent);

                            break;

                        case STAT_LOG_EVENT_EVENT:

                            processed = processStatLogEventEvent(queueEvent);

                            break;

                        case WORD_DICTIONARY_MISSING_WORD_EVENT:

                            processed = processWordDictionaryMissingWordEvent(queueEvent);

                            break;

                        default:
                            // noop
                    }

                    if (processed == true) {
                        dataManager.deleteQueueEvent(queueEvent);

                        counter = 0;
                        sleepMode = 1;
                    }
                }
            }

            long sleepTime;

            if (sleepMode == 0) {
                sleepTime = 5000;

            } else if (sleepMode == 1) {
                sleepTime = 1000;

            } else {
                sleepTime = 5000;
            }

            try {
                Thread.sleep(sleepTime);

                counter++;

            } catch (InterruptedException e) {
                // noop
            }
        }
    }

    //

    public void addQueueEvent(Activity activity, IQueueEvent queueEvent) {

        DataManager dataManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(activity).getDataManager();

        if (dataManager != null) {
            dataManager.addQueueEvent(queueEvent);
        }
    }

    //

    private boolean processWordDictionaryMissingWordEvent(IQueueEvent queueEvent) {

        WordDictionaryMissingWordEvent wordDictionaryMissingWordEvent = (WordDictionaryMissingWordEvent)queueEvent;

        //

        ServerClient serverClient = new ServerClient();

        return serverClient.sendMissingWord(packageInfo, wordDictionaryMissingWordEvent.getWord(), wordDictionaryMissingWordEvent.getWordPlaceSearch());
    }

    private boolean processStatLogScreenEvent(IQueueEvent queueEvent) {

        ServerClient serverClient = new ServerClient();

        return serverClient.sendQueueEvent(packageInfo, queueEvent);
    }

    private boolean processStatLogEventEvent(IQueueEvent queueEvent) {

        ServerClient serverClient = new ServerClient();

        return serverClient.sendQueueEvent(packageInfo, queueEvent);
    }

    private boolean processStatStartAppEvent(IQueueEvent queueEvent) {

        ServerClient serverClient = new ServerClient();

        return serverClient.sendQueueEvent(packageInfo, queueEvent);
    }
}
