package com.hnu.util;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * JavaMail工具类，用于在Java中发送邮件
 */
public class Email {

    public static String USERNAME = "2387149564@qq.com"; //	邮箱发送账号
    public static String PASSWORD = "hujajluabgxidihi" ; //邮箱平台的授权码
    public static String HOST = "smtp.qq.com"; // SMTP服务器地址
    public static String PORT = "25"; //SMTP服务器端口
    public static Session session = null;

    /**
     * 创建Sesssion
     */
    public static void createSession() {

        if (session != null) return;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST); //SMTP主机名
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    /**
     * 发送纯文本邮件，单人发送
     *
     * @param title
     * @param content
     * @param toMail
     */
    public static void postMessage(String title, String content, String toMail) {
        try {
            createSession();
            //构造邮件主体
            MimeMessage message = new MimeMessage(session);
            message.setSubject(title);
            message.setText(content);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
            //发送
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}










