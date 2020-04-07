package com.centaurstech.domain.eventtrack;

import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author feli
 */
public class EventTrackSetHandler extends EventTrackHandler {

    private final HashSet<EventTrack> eventTrackSet;

    public EventTrackSetHandler(EventTrackProxy.EventTrackSender eventTrackSender, int reportSize) {
        super(eventTrackSender, reportSize);
        eventTrackSet = new HashSet<>(reportSize);
    }

    @Override
    public void submitTask(EventTrack eventTrack) {
        synchronized (eventTrackSet) {
            checkTrack(reportSize);
            eventTrackSet.add(eventTrack);
        }
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
        if (eventTrackSet.size() >= needReportSize) {
            HashSet<EventTrack> temp = new HashSet<>(eventTrackSet);
            Runnable runnable = () -> eventTrackSender.sendEventsToServer(temp);
            FutureTask<Void> task = new FutureTask<>(runnable, null);
            cachePool.execute(task);
            eventTrackSet.clear();
            lastFlush = Calendar.getInstance().getTime();
            return task;
        }
        return null;
    }

}
