package com.centaurstech.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QueryHelperTest {


    @Test
    public void testEncodeURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put("a", "1");
        query.put("b", "2");
        query.put("c", "3");

        String queryString = QueryHelper.urlEncodeUTF8(query);
        System.out.println(queryString);
        assertThat(queryString, is("a=1&b=2&c=3"));

        Map<String, String> decodedQuery =  QueryHelper.urlDecodeUTF8Map(queryString);
        System.out.println(decodedQuery);
        assertThat(decodedQuery.size(), is(3));
    }

}
