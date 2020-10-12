package com.centaurstech.utils;

import com.centaurstech.domain.EngineQuery;
import com.centaurstech.domain.EngineQueryProxy;
import com.centaurstech.utils.dotrecord.DotRecorder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Fangzhou.Long on 10/30/2019
 * @project common
 */
public class DotRecorderTest {

    @Test
    public void testOutput() throws InterruptedException {
        EngineQuery engineQuery = new EngineQueryProxy("SingleTest");

        DotRecorder dotRecorder = new DotRecorder("SingleTest");
        dotRecorder.dot("handling");
        Thread.sleep(1000);
        dotRecorder.dot("after_sleep");
        System.out.println(dotRecorder.getResultAndDestroy());

        System.out.println(engineQuery.getQueryTimeString());
    }

    public FutureTask buildFutureTask() {
        return new FutureTask(() -> {
            DotRecorder dotRecorder = DotRecorder.getInstance("MultiTest");

            for (int i=1; i<=100000; i++) { dotRecorder.dot("test" + i); }

            System.out.println(dotRecorder.dots.size() + " " + dotRecorder.getTotal());

            dotRecorder.destroy();
            // DotRecorder.destroyInstance("MultiTest");
            // DotRecorder.cleanInstances();
            return null;
        });
    }

    @Test
    public void testHuge() throws ExecutionException, InterruptedException {
        EngineQuery engineQuery = new EngineQueryProxy("MultiTest");

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                10, 1000,
                100, MILLISECONDS,
                new ArrayBlockingQueue<>(100));

        ArrayList<FutureTask> tasks = new ArrayList<>();
        for (int i=1; i<=100; i++) {
            FutureTask task = buildFutureTask();
            tasks.add(task);
            tpe.submit(task);
        }

        for (FutureTask task : tasks) {
            task.get();
        }
        DotRecorder.cleanInstances();

        System.out.println(engineQuery.getQueryTimeString());
    }

}
