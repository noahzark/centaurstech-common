package com.centaurstech.utils.encode;

import com.centaurstech.domain.BasicAuthData;
import org.junit.Test;

public class HttpUtilTest {

    @Test
    //解码http查询字符串
    public void testDecodeHttpQuery() throws Exception {
        System.out.println(HttpUtil.decodeHttpQuery("size=10&stateType=100&lastId=&page=8&test1&test2=&aaa=123"));
    }

    @Test
    //解码BasicAuth字符串
    public void testDecryptBasicAuth() throws Exception {
        BasicAuthData baiscAuthData = BasicAuthData.fromString("Basic MTg4MTk0NDgyNzQ6NTMyMw==");
        System.out.println(baiscAuthData);
    }

}
