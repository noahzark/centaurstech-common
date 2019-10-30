package com.centaurstech.utils.dotrecord;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Fangzhou.Long on 10/30/2019
 * @project robot.business.proxy
 */
public class DotRecorder {

    public String name;
    public List<DotRecord> dots;
    public long lastDotTime;
    public long recordStart;

    public DotRecorder(String name) {
        this.name = name;
        dots = new LinkedList<>();
        lastDotTime = -1;
        recordStart = System.currentTimeMillis();
    }

    public DotRecord dot(String dotName) {
        DotRecord dotRecord = new DotRecord(dotName, lastDotTime);
        dots.add(dotRecord);
        lastDotTime = System.currentTimeMillis();
        return dotRecord;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (DotRecord d : dots) {
            sb.append("\t");
            sb.append(d);
            sb.append("\n");
        }
        sb.append("}");

        return MessageFormat.format(
                "{0}\n{1}\ntotal: {2}",
                name, sb, System.currentTimeMillis() - recordStart);
    }

}
