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

    public static int TASK_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    public static int EVENT_PER_TASK = 100000;

    static ConcurrentHashMap<EventTrack, Boolean> reports = new ConcurrentHashMap<>();

    public FutureTask<Void> buildFutureTask(EventTrackServiceHashSet eventSendSet) {
        return new FutureTask<>(() -> {
            int times = 0;
            while (++times <= EVENT_PER_TASK) {
                eventSendSet.addBotActivationEvent("10086", SMART_LIFE_APP, UNKNOWN);
            }
            return null;
        });
    }

    @Test
    //线程测试哈希集合模块
    public void testHashSetMode() throws Exception {
        reports = new ConcurrentHashMap<>(TASK_COUNT * EVENT_PER_TASK);
        EngineQuery engineQuery = new EngineQueryProxy("HashSet Mode");

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), TASK_COUNT,
                100, MILLISECONDS,
                new ArrayBlockingQueue<>(100));
        EventTrackServiceHashSet eventSendSet = new EventTrackServiceHashSet();

        ArrayList<FutureTask<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < TASK_COUNT; i++) {
            FutureTask<Void> futureTask = buildFutureTask(eventSendSet);
            tasks.add(futureTask);
            tpe.submit(futureTask);
        }

        for (FutureTask<Void> task : tasks) {
            task.get();
        }
        System.out.println(engineQuery.getQueryTimeString());

        eventSendSet.getEventTrackHandler().forceFlushAsync(0);
        System.out.println(reports.size());
    }

    public FutureTask<Boolean> buildFutureTask(EventTrackServiceBlockingQueue eventSendSet) {
        return new FutureTask<>(() -> {
            int times = 0;
            while (++times <= EVENT_PER_TASK) {
                eventSendSet.addBotActivationEvent("10086", SMART_LIFE_APP, UNKNOWN);
            }
            return true;
        });
    }

    @Test
    //线程测试阻塞队列模块
    public void testBlockingQueueMode() throws Exception {
        reports = new ConcurrentHashMap<>(TASK_COUNT * EVENT_PER_TASK);
        EngineQuery engineQuery = new EngineQueryProxy("ArrayBlockingQueue Mode");

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), TASK_COUNT,
                100, MILLISECONDS,
                new ArrayBlockingQueue<>(100));
        EventTrackServiceBlockingQueue eventSendSet = new EventTrackServiceBlockingQueue();

        ArrayList<FutureTask<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < TASK_COUNT; i++) {
            FutureTask<Boolean> futureTask = buildFutureTask(eventSendSet);
            tasks.add(futureTask);
            tpe.submit(futureTask);
        }

        for (FutureTask<Boolean> task : tasks) {
            task.get();
        }
        System.out.println(engineQuery.getQueryTimeString());

        eventSendSet.getEventTrackHandler().forceFlushAsync(0);
        System.out.println(reports.size());
        reports.clear();
    }

    class EventTrackServiceHashSet extends EventTrackProxy {
        EventTrackServiceHashSet() {
            super((eventTracks) -> {
                for (EventTrack eventTrack : eventTracks) {
                    reports.put(eventTrack, false);
                }
            }, "Test", EVENT_PER_TASK, HASH_SET);
        }
    }

    class EventTrackServiceBlockingQueue extends EventTrackProxy {
        EventTrackServiceBlockingQueue() {
            super((eventTracks) -> {
                for (EventTrack eventTrack : eventTracks) {
                    reports.put(eventTrack, false);
                }
            }, "Test", EVENT_PER_TASK);
        }
    }

}
