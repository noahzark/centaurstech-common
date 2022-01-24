package com.centaurstech.utils.email;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface IEmailClient {

    boolean send(String to, String title, String content);

    boolean send(List<String> to, String title, String content);

    boolean send(String to, String title, String content, List<File> files);

    boolean send(List<String> to, String title, String content, List<File> files);

    /**
     *
     * @param to 目标邮箱
     * @param title 标题
     * @param content 内容
     * @param fileInputStream  文件输入流
     * @param fileName 文件名
     * @return
     */
    boolean send(List<String> to, String title, String content, InputStream fileInputStream, String fileName);

}
