package com.centaurstech.utils;

import com.centaurstech.domain.EngineQuery;
import com.centaurstech.domain.EngineQueryProxy;
import com.centaurstech.utils.dotrecord.DotRecorder;
import org.junit.Test;

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
        System.out.println(dotRecorder);

        System.out.println(engineQuery.getQueryTimeString());
    }

    @Test
    public void testHuge() {
        EngineQuery engineQuery = new EngineQueryProxy("MultiTest");

        DotRecorder dotRecorder = new DotRecorder("MultiTest");
        for (int i=1; i<=100000; i++) { dotRecorder.dot("test" + i); }

        System.out.println(engineQuery.getQueryTimeString());
    }

}
