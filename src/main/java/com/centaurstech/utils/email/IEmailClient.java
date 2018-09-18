package com.centaurstech.utils.email;

public interface IEmailClient {

    boolean send(String to, String title, String content);

}
