package com.centaurstech.domain.eventtrack;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author feli
 */
public abstract class EventTrackHandler {

    ExecutorService cachePool = Executors.newCachedThreadPool();

    EventTrackProxy.EventTrackSender eventTrackSender;
    int reportSize;

    Date lastFlush;

    public EventTrackHandler(EventTrackProxy.EventTrackSender eventTrackSender, int reportSize) {
        this.eventTrackSender = eventTrackSender;
        this.reportSize = reportSize;
    }

    public abstract void submitTask(EventTrack eventTrack);

    public abstract void forceFlush(int needReportSize);

    public abstract void forceFlushAsync(int needReportSize) throws ExecutionException, InterruptedException;

    public abstract Future<Void> checkTrack(int needReportSize);

}
