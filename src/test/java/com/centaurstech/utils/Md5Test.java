package com.centaurstech.utils;

import org.junit.Test;
import com.centaurstech.utils.encode.Md5;

/**
 * Created by Feliciano on 7/3/2018.
 */
public class Md5Test {

    //md5加密入参
    @Test
    public void testMd5() throws Exception {
        Md5.digest("test");
    }
}
