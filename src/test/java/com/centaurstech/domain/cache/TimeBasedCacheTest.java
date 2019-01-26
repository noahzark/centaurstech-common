package com.centaurstech.domain.cache;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TimeBasedCacheTest {

    @Test
    public void testCache() {
        TimeBasedCache<String> timeBasedCache = new TimeBasedCache<>(100);
        timeBasedCache.put("test", "test");
        assertThat(timeBasedCache.contains("test"), is(true));
    }

}
