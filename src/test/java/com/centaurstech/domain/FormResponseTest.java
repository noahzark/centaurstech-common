package com.centaurstech.domain;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author hundun
 * Created on 2023/02/09
 */
public class FormResponseTest {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void oldUsage() {
        // create
        FormResponse formResponse = new FormResponse();
        formResponse.setData(new FormResponse.FormData<Map<String, Integer>, Void>());
        // test runtime
        FormResponse.FormData<Map<String, Integer>, Void> formData = formResponse.getData();
        formData.setVars(new HashMap<>());
        Map<String, Integer> formDataVars = (Map<String, Integer>)formResponse.getData().getVars();
        formDataVars.put("foo", 114);
    }
    
    @Test
    public void newUsage() {
        // create
        FormResponse<Map<String, Integer>, Void> formResponse = new FormResponse<>();
        formResponse.setData(new FormResponse.FormData<>());
        // test runtime
        FormResponse.FormData<Map<String, Integer>, Void> formData = formResponse.getData();
        formData.setVars(new HashMap<>());
        Map<String, Integer> formDataVars = formResponse.getData().getVars();
        formDataVars.put("foo", 114);
    }

}
