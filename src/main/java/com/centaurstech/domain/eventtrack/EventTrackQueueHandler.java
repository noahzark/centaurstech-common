package com.centaurstech.domain.eventtrack;

import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.*;

/**
 * @author feli
 */
public class EventTrackQueueHandler extends EventTrackHandler {

    private LinkedBlockingQueue<EventTrack> eventTrackQueue;
    private ExecutorService cachePool = Executors.newCachedThreadPool();

    public EventTrackQueueHandler(EventTrackProxy.EventTrackSender eventTrackSender, int reportSize) {
        super(eventTrackSender, reportSize);
        eventTrackQueue = new LinkedBlockingQueue<>(reportSize * 2);
    }

    @Override
    public void submitTask(EventTrack eventTrack) {
        checkTrack(reportSize);
        eventTrackQueue.add(eventTrack);
    }

    @Override
    public void forceFlush(int needReportSize) {
        checkTrack(needReportSize);
    }

    @Override
    public void forceFlushAsync(int needReportSize) throws ExecutionException, InterruptedException {
        Future<Void> flushResult = checkTrack(needReportSize);
        if (flushResult != null) {
            flushResult.get();
        }
    }

    @Override
    public Future<Void> checkTrack(int needReportSize) {
        if (eventTrackQueue.size() > needReportSize) {
            HashSet<EventTrack> eventSendSet = new HashSet<>(eventTrackQueue.size());
            eventTrackQueue.drainTo(eventSendSet);
            Runnable runnable = () -> eventTrackSender.sendEventsToServer(eventSendSet);
            FutureTask<Void> task = new FutureTask<>(runnable, null);
            cachePool.execute(task);
            lastFlush = Calendar.getInstance().getTime();
            return task;
        }
        return null;
    }

}
