package com.centaurstech.domain.cache;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeBasedCache<Content> {

    Long expireIn;

    Map<String, Content> container;

    Map<String, Long> lastUpdate;

    public TimeBasedCache(Long expireIn) {
        this.expireIn = expireIn;
        container = new HashMap<>();
        lastUpdate = new HashMap<>();
    }

    public void put(String key, Content content) {
        container.put(key, content);
        lastUpdate.put(key, Calendar.getInstance().getTimeInMillis());
    }

    public Content get(String key) {
        if (lastUpdate.containsKey(key)) {
            if (Calendar.getInstance().getTimeInMillis() < lastUpdate.get(key) + expireIn) {
                return container.get(key);
            }
            lastUpdate.remove(key);
        }
        return null;
    }

}
