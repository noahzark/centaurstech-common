package com.centaurstech.utils.email;
import com.sun.mail.util.MailSSLSocketFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Created by Feliciano on 11/22/2017.
 *
 * Refactored by Feliciano Long on 09/17/2018
 */
public class EmailClient implements IEmailClient {

    EmailClientAuthenticator emailClientAuthenticator;

    String smptHost;

    String from;

    public EmailClient(String smptHost) {
        this(smptHost, null);
    }

    public EmailClient(String smptHost, String from) {
        this.smptHost = smptHost;
        this.from = from;
    }

    public void setAuth(String username, String password) {
        this.emailClientAuthenticator = new EmailClientAuthenticator(username, password);
        if (from == null) {
            from = username;
        }
    }

    @Override
    public boolean send(String to, String title, String content) {

    }

    public boolean send(String to, String title, String content, List<File> files) {
        //String from = "server@centaurstech.com";//change accordingly
        //String host = "smtp.exmail.qq.com";//or IP address

        // Get the session object
        Properties properties = System.getProperties();

        // Set properties
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", smptHost);
        properties.setProperty("mail.smtp.port", "465");

        // Enalbe Auth
        properties.setProperty("mail.smtp.auth", "true");

        // Enable SSL
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }

        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(properties, emailClientAuthenticator);

        //compose the message
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);
            message.setText(content);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if (files != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                for (File file : files) {
                    attachmentPart.attachFile(file);
                }
                multipart.addBodyPart(attachmentPart);
            }
            message.setContent(multipart);

            // Send message
            Transport.send(message);
        }catch (MessagingException | IOException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String [] args){
        EmailClient emailClient = new EmailClient(
                "smtp.exmail.qq.com",
                "server@centaurstech.com");
        emailClient.setAuth(
                "test@test.com",
                "test123456");

        boolean result = emailClient.send(
                "server@centaurstech.com",
                "Ping",
                "test email");
        if (result) {
            System.out.println("message sent successfully....");
        }
    }

}