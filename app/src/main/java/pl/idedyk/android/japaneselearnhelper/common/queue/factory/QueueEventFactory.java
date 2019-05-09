package pl.idedyk.android.japaneselearnhelper.common.queue.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.idedyk.android.japaneselearnhelper.common.queue.event.IQueueEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.QueueEventOperation;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.StatLogEventEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.StatLogScreenEvent;
import pl.idedyk.android.japaneselearnhelper.common.queue.event.WordDictionaryMissingWordEvent;

public class QueueEventFactory implements IQueueEventFactory {

    @Override
    public IQueueEvent createQueueEvent(String uuid, String operation, String createDateString, String params) {

        if (operation == null || createDateString == null) {
            return null;
        }

        QueueEventOperation queueEventOperation = null;

        try {
            queueEventOperation = QueueEventOperation.valueOf(operation);

        } catch (IllegalArgumentException e) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(IQueueEvent.dateFormat);

        Date createDateDate = null;

        try {
            createDateDate = sdf.parse(createDateString);

        } catch (ParseException e) {
            return null;
        }

        //

        IQueueEvent queueEvent = null;

        switch (queueEventOperation) {

            case STAT_LOG_SCREEN_EVENT:

                queueEvent = new StatLogScreenEvent(uuid, createDateDate, params);

                break;

            case STAT_LOG_EVENT_EVENT:

                queueEvent = new StatLogEventEvent(uuid, createDateDate, params);

                break;

            case WORD_DICTIONARY_MISSING_WORD_EVENT:

                queueEvent = new WordDictionaryMissingWordEvent(uuid, createDateDate, params);

                break;

            default:
                return null;
        }

        return queueEvent;
    }
}
