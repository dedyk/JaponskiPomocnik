package pl.idedyk.android.japaneselearnhelper.common.queue.factory;

import java.util.Date;

import pl.idedyk.android.japaneselearnhelper.common.queue.event.IQueueEvent;

public interface IQueueEventFactory {
    public IQueueEvent createQueueEvent(String uuid, String operation, String createDate, String params);
}
