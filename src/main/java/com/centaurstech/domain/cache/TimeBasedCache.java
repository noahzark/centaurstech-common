package com.centaurstech.domain.cache;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeBasedCache<Content> {

    public static final long DEFAULT_EXPIRE_IN = 600 * 1000L;

    Long expireIn;

    Map<String, Content> container;

    Map<String, Long> expireWhen;

    public TimeBasedCache(Long expireInMillis) {
        this.expireIn = expireInMillis;
        container = new HashMap<>();
        expireWhen = new HashMap<>();
    }

    public TimeBasedCache(int expireInSeconds) {
        this(expireInSeconds * 1000L);
    }

    public TimeBasedCache() {
        this(DEFAULT_EXPIRE_IN);
    }

    public void put(String key, Content content) {
        this.put(key, content, expireIn);
    }

    public void put(String key, Content content, Long expireIn) {
        container.put(key, content);
        expireWhen.put(key, Calendar.getInstance().getTimeInMillis() + expireIn);
    }

    public Content get(String key) {
        if (expireWhen.containsKey(key)) {
            if (Calendar.getInstance().getTimeInMillis() < expireWhen.get(key)) {
                return container.get(key);
            }
            container.remove(key);
            expireWhen.remove(key);
        }
        return null;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

}
