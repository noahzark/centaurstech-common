package com.centaurstech.utils.dynamicproxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 经过多次试验，可以看出平均情况下的话，JDK动态代理的运行速度已经逐渐提高了，在低版本的时候，运行的性能可能不如CGLib，但是在1.8版本中运行多次，基本都可以得到一致的测试结果，那就是JDK动态代理已经比CGLib动态代理快了！
 *
 * 但是JDK动态代理和CGLib动态代理的适用场景还是不一样的哈！
 *
 * 六、总结
 * 最终的测试结果大致是这样的，在1.6和1.7的时候，JDK动态代理的速度要比CGLib动态代理的速度要慢，但是并没有教科书上的10倍差距，在JDK1.8的时候，JDK动态代理的速度已经比CGLib动态代理的速度快很多了，希望小伙伴在遇到这个问题的时候能够有的放矢！
 *
 * Spring AOP中的JDK和CGLib动态代理关于这个知识点很重要，关于两者之间性能的对比经过测试实验已经有了一个初步的结果，以后再有人问你Spring AOP，不要简单的说JDK动态代理和CGLib这两个了，是时候的可以抛出来对两者之间区别的理解，是有加分的哦！
 * ---------------------
 * @author 徐刘根
 * https://blog.csdn.net/xlgen157387/article/details/82497594
 * 来源：CSDN
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */
public class ProxyPerformanceTest {

    public static void main(String[] args) {
        //创建测试对象
        Target nativeTest = new TargetImpl();
        Target dynamicProxy = JdkDynamicProxyTest.newProxyInstance(nativeTest);
        Target cglibProxy = CglibProxyTest.newProxyInstance(TargetImpl.class);

        //预热一下
        int preRunCount = 10000;
        runWithoutMonitor(nativeTest, preRunCount);
        runWithoutMonitor(cglibProxy, preRunCount);
        runWithoutMonitor(dynamicProxy, preRunCount);

        //执行测试
        Map<String, Target> tests = new LinkedHashMap<String, Target>();
        tests.put("Native   ", nativeTest);
        tests.put("Dynamic  ", dynamicProxy);
        tests.put("Cglib    ", cglibProxy);
        int repeatCount = 3;
        int runCount = 1000000;
        runTest(repeatCount, runCount, tests);
        runCount = 50000000;
        runTest(repeatCount, runCount, tests);
    }

    private static void runTest(int repeatCount, int runCount, Map<String, Target> tests) {
        System.out.println(
                String.format("\n===== run test : [repeatCount=%s] [runCount=%s] [java.version=%s] =====",
                        repeatCount, runCount, System.getProperty("java.version")));
        for (int i = 0; i < repeatCount; i++) {
            System.out.println(String.format("\n--------- test : [%s] ---------", (i + 1)));
            for (String key : tests.keySet()) {
                runWithMonitor(tests.get(key), runCount, key);
            }
        }
    }

    private static void runWithoutMonitor(Target target, int runCount) {
        for (int i = 0; i < runCount; i++) {
            target.test(i);
        }
    }

    private static void runWithMonitor(Target target, int runCount, String tag) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < runCount; i++) {
            target.test(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("[" + tag + "] Total Time:" + (end - start) + "ms");
    }
}
