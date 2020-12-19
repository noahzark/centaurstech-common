package com.centaurstech.domain;

/**
 * All centaurs backend api result must implement this interface
 */
public interface IApiResult {

    Integer getRetcode();

    String getMessage();

    Integer getStatus();

    String getPayload();

    default Integer getEncrypt() { return 0; }

}
