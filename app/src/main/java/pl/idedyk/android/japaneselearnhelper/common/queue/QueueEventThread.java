package pl.idedyk.android.japaneselearnhelper.common.queue;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.IQueueEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.WordDictionaryMissingWordEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.factory.QueueEventFactory;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;

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

            if (stop == true || counter > 120) {
                break;
            }

            DataManager dataManager = JapaneseAndroidLearnHelperApplication.getInstance().getDataManager();

            if (dataManager != null) {

                List<IQueueEvent> queueEventList = dataManager.getQueueEventList(queueEventFactory);

                for (IQueueEvent queueEvent : queueEventList) {

                    counter = 0;

                    Log.d("JapaneseAndroidLearn", "QueueEventThread - processing: " + queueEvent.getUUID() + " - " + queueEvent.getQueryEventOperation() + " - " + queueEvent.getParamsAsString());

                    boolean processed = false;

                    // przetwarzamy zdarzenie
                    switch (queueEvent.getQueryEventOperation()) {

                        case STAT_LOG_SCREEN_EVENT:

                            // noop
                            processed = true;

                            break;

                        case STAT_LOG_EVENT_EVENT:

                            // noop
                            processed = true;

                            break;

                        case WORD_DICTIONARY_MISSING_WORD_EVENT:

                            processed = processWordDictionaryMissingWordEvent(queueEvent);

                            break;

                        default:
                            // noop
                    }

                    if (processed == true) {
                        dataManager.deleteQueueEvent(queueEvent.getUUID());
                    }
                }
            }

            try {
                Thread.sleep(1000);

                counter++;

            } catch (InterruptedException e) {
                // noop
            }
        }
    }

    //

    public void addQueueEvent(Activity activity, IQueueEvent queueEvent) {

        DataManager dataManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(activity).getDataManager();

        dataManager.addQueueEvent(queueEvent);
    }

    private boolean processWordDictionaryMissingWordEvent(IQueueEvent queueEvent) {

        WordDictionaryMissingWordEvent wordDictionaryMissingWordEvent = (WordDictionaryMissingWordEvent)queueEvent;

        //

        ServerClient serverClient = new ServerClient();

        return serverClient.sendMissingWord(packageInfo, wordDictionaryMissingWordEvent.getWord(), wordDictionaryMissingWordEvent.getWordPlaceSearch());
    }
}
