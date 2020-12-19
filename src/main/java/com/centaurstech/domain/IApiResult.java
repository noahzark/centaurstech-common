package com.centaurstech.domain;

/**
 * All centaurs backend api result must implement this interface
 */
public interface IApiResult {

    /**
     * api return code, 0 means sucess, otherwise failed
     * @return
     */
    Integer getRetcode();

    /**
     * hint message
     * @return
     */
    String getMessage();

    /**
     * http status, 200 means OK, check https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
     * @return
     */
    Integer getStatus();

    /**
     * response data body
     * @return
     */
    String getPayload();

    /**
     * encrypt mode version, 0 means no encryption
     * @return
     */
    default Integer getEncrypt() { return 0; }

}
