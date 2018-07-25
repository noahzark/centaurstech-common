package com.centaurstech.domain;

public interface ApiResult {

    Integer getRetcode();

    String getMessage();

    Integer getStatus();

    String getPayload();

}
