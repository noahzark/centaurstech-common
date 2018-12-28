package com.centaurstech.domain;

import com.centaurstech.domain.eventtrack.EventTrack;
import com.centaurstech.domain.eventtrack.EventTrackProxy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import static com.centaurstech.domain.eventtrack.EventTrackItem.BotName.UNKNOWN;
import static com.centaurstech.domain.eventtrack.EventTrackItem.Platform.SMART_LIFE_APP;
import static com.centaurstech.domain.eventtrack.EventTrackProxy.BuffMode.HASH_SET;
import static com.centaurstech.domain.eventtrack.EventTrackProxy.BuffMode.LINKED_BLOCKING_QUEUE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class EventTrackTest {

    static ConcurrentHashMap<EventTrack, Boolean> reports = new ConcurrentHashMap<>();

    @Deprecated
    public FutureTask<Void> buildFutureTask(EventTrackServiceHashSet eventSendSet) {
        return new FutureTask(() -> {
            int times = 0;
            while (++times <= 10000) {
                eventSendSet.addBotActivationEvent("10086", SMART_LIFE_APP, UNKNOWN);
            }
            return null;
        });
    }

    @Test
    public void testHashSetMode() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("HashSet Mode");
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                100, 1000,
                100, MILLISECONDS,
                new ArrayBlockingQueue<>(100));
        EventTrackServiceHashSet eventSendSet = new EventTrackServiceHashSet();

        ArrayList<FutureTask<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            FutureTask<Void> futureTask = buildFutureTask(eventSendSet);
            tasks.add(futureTask);
            tpe.submit(futureTask);
        }
        for (FutureTask<Void> task : tasks) {
            task.get();
        }
        eventSendSet.forceFlush(0);

        System.out.println(engineQuery.getQueryTimeString());
        System.out.println(reports.size());
        reports.clear();
    }

    public FutureTask<Boolean> buildFutureTask(EventTrackServiceBlockingQueue eventSendSet) {
        return new FutureTask(() -> {
            int times = 0;
            while (++times <= 10000) {
                eventSendSet.addBotActivationEvent("10086", SMART_LIFE_APP, UNKNOWN);
            }
            return true;
        });
    }

    @Test
    public void testBlockingQueueMode() throws Exception {
        EngineQuery engineQuery = new EngineQueryProxy("ArrayBlockingQueue Mode");
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                100, 1000,
                100, MILLISECONDS,
                new ArrayBlockingQueue<>(100));
        EventTrackServiceBlockingQueue eventSendSet = new EventTrackServiceBlockingQueue();

        ArrayList<FutureTask<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            FutureTask<Boolean> futureTask = buildFutureTask(eventSendSet);
            tasks.add(futureTask);
            tpe.submit(futureTask);
        }
        for (FutureTask<Boolean> task : tasks) {
            task.get();
        }
        eventSendSet.forceFlush(0);

        System.out.println(engineQuery.getQueryTimeString());
        System.out.println(reports.size());
        reports.clear();
    }

    @Deprecated
    class EventTrackServiceHashSet extends EventTrackProxy {
        public EventTrackServiceHashSet() {
            super((eventTracks) -> {
                for (EventTrack eventTrack : eventTracks) {
                    reports.put(eventTrack, false);
                }
            }, "Test", 100, HASH_SET);
        }
    }

    class EventTrackServiceBlockingQueue extends EventTrackProxy {
        public EventTrackServiceBlockingQueue() {
            super((eventTracks) -> {
                for (EventTrack eventTrack : eventTracks) {
                    reports.put(eventTrack, false);
                }
            }, "Test", 100, LINKED_BLOCKING_QUEUE);
        }
    }

}
