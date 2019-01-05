package com.centaurstech.utils;

import com.centaurstech.domain.BasicAuthData;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class StringExtractorTest {

    @Test
    public void testBasicAuth() throws Exception {
        BasicAuthData basicAuthContent = BasicAuthData.fromString("Basic dGVzdDoxMjM0NTY=");
        assertThat(basicAuthContent.getUsername(), is("test"));
        assertThat(basicAuthContent.getPassword(), is("123456"));

        assertThat(basicAuthContent.toString().equals(BasicAuthData.fromAuth("test", "123456").toString()), is(true));

        basicAuthContent = BasicAuthData.fromString("Basic dGVzdDo=");
        assertThat(basicAuthContent.getUsername(), is("test"));
        assertThat(basicAuthContent.getPassword(), is(""));

        basicAuthContent = BasicAuthData.fromString("dGVzdDo=");
        assertThat(basicAuthContent, is(IsNull.nullValue()));
    }

    @Test
    public void testAccessToken() throws Exception {
        assertThat(StringExtractor.extractAuthToken("Bearer 6f97d8a3-3c8e-4990-8a5c-b5ffa1b9f161"),
                is("6f97d8a3-3c8e-4990-8a5c-b5ffa1b9f161"));
    }

}
