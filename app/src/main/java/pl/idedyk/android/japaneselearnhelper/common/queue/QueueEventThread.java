package pl.idedyk.android.japaneselearnhelper.common.queue;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.IQueueEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.factory.QueueEventFactory;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;

public class QueueEventThread extends Thread {

    private boolean stop = false;

    public QueueEventThread() {
        // noop
    }

    public void requestStop() {
        this.stop = true;

        Log.d("JapaneseAndroidLearnHel", "requestStop");
    }

    @Override
    public void run() {

        QueueEventFactory queueEventFactory = new QueueEventFactory();

        while(true) {

            if (stop == true) {
                break;
            }

            DataManager dataManager = JapaneseAndroidLearnHelperApplication.getInstance().getDataManager();

            if (dataManager != null) {

                List<IQueueEvent> queueEventList = dataManager.getQueueEventList(queueEventFactory);

                for (IQueueEvent queueEvent : queueEventList) {

                    Log.d("AAAAA", "BBBB: " + queueEvent.getUUID() + " - " + queueEvent.getQueryEventOperation() + " - " + queueEvent.getParamsAsString());

                    boolean processed = false;

                    // przetwarzamy zdarzenie
                    switch (queueEvent.getQueryEventOperation()) {

                        case STAT_LOG_SCREEN_EVENT:


                            break;

                        case STAT_LOG_EVENT_EVENT:


                            break;

                        default:
                            // noop
                    }

                    processed = true; // !!!!!!!!!!!!!!! FIXME

                    if (processed == true) {
                        dataManager.deleteQueueEvent(queueEvent.getUUID());
                    }
                }
            }

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // noop
            }
        }
    }

    //

    public void addQueueEvent(Activity activity, IQueueEvent queueEvent) {

        JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(activity).getDataManager();

        dataManager.addQueueEvent(queueEvent);
    }
}
