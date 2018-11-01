package com.centaurstech.utils.encode;

import org.junit.Test;

public class HttpUtilTest {

    @Test
    public void testDecodeHttpQuery() throws Exception {
        System.out.println(HttpUtil.decodeHttpQuery("size=10&stateType=100&lastId=&page=8"));
    }

}
