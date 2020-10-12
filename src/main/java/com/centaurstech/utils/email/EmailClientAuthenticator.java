package com.centaurstech.utils.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailClientAuthenticator extends Authenticator {
    String u;
    String p;

    public EmailClientAuthenticator(String u, String p){
        this.u=u;
        this.p=p;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(u,p);
    }

}