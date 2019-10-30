package com.centaurstech.utils.dotrecord;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Fangzhou.Long on 10/30/2019
 * @project robot.business.proxy
 */
public class DotRecorder {

    public String name;
    public List<DotRecord> dots;
    public long lastDotTime;
    public long recordStart;

    static final ThreadLocal<Map<String, DotRecorder>> sThreadLocal = new ThreadLocal<>();

    /**
     * get instance from thread local, remember to destroy before thread exits
     * @param name
     * @return
     */
    public static DotRecorder getInstance(String name) {
        if (sThreadLocal.get() == null) {
            sThreadLocal.set(new HashMap<>(10));
        }
        if (!sThreadLocal.get().containsKey(name)) {
            sThreadLocal.get().put(name, new DotRecorder(name));
        }
        return sThreadLocal.get().get(name);
    }

    /**
     * destroy an instance
     * @param name
     */
    public static void destroyInstance(String name) {
        if (sThreadLocal.get() != null) {
            sThreadLocal.get().remove(name);
        }
    }

    /**
     * clean up all instances
     */
    public static void cleanInstances() {
        if (sThreadLocal.get() != null) {
            sThreadLocal.remove();
        }
    }

    /**
     * construct a dot recorder
     * @param name
     */
    public DotRecorder(String name) {
        this.name = name;
        dots = new LinkedList<>();
        lastDotTime = -1;
        recordStart = System.currentTimeMillis();
    }

    /**
     * record a dot with dot name
     * @param dotName
     * @return
     */
    public DotRecord dot(String dotName) {
        DotRecord dotRecord = new DotRecord(dotName, lastDotTime);
        dots.add(dotRecord);
        lastDotTime = System.currentTimeMillis();
        return dotRecord;
    }

    /**
     * get runtime so far
     * @return
     */
    public long getTotal() {
        return System.currentTimeMillis() -  recordStart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (DotRecord d : dots) {
            sb.append("\t");
            sb.append(d);
            sb.append("\n");
        }
        sb.append("}");

        return MessageFormat.format(
                "{0}\n{1}\ntotal: {2}",
                name, sb, getTotal());
    }

    /**
     * get result and destroy instance
     * @return
     */
    public String getResultAndDestroy() {
        String result = toString();
        destroyInstance(name);
        return result;
    }

    /**
     * directly destroy instance
     */
    public void destroy() {
        destroyInstance(name);
    }

}
