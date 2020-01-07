package com.centaurstech.utils.dotrecord;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Fangzhou.Long on 10/30/2019
 * @project robot.business.proxy
 */
public class DotRecord {

    public String dotName;
    public LocalDateTime dotDatetime;
    public long interval;

    public DotRecord() {
    }

    public DotRecord(String dotName, long lastDotTime) {
        long timestamp = System.currentTimeMillis();
        if (lastDotTime <= 0) {
            lastDotTime = timestamp;
        }

        this.dotName = dotName;
        this.dotDatetime = ZonedDateTime.now(ZoneId.of("GMT+8")).toLocalDateTime();
        this.interval = timestamp - lastDotTime;
    }

    @Override
    public String toString() {
        return MessageFormat.format("date: {0} {1}={2}", dotDatetime.toString(), dotName, interval);
    }

}
