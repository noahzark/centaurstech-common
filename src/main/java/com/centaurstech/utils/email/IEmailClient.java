package com.centaurstech.utils.email;

import java.io.File;
import java.util.List;

public interface IEmailClient {

    boolean send(String to, String title, String content);

    boolean send(String to, String title, String content, List<File> files);

}
