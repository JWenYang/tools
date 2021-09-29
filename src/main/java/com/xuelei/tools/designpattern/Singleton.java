package com.xuelei.tools.designpattern;

import cn.hutool.core.io.IoUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Singleton {

    //私有构造方法，防止被实例化
    private Singleton(){}

    //使用内部类维护单例
    private static class SingletonFactory{
        private static Singleton singleton = new Singleton();
    }

    //获取实例
    public static Singleton getInstance(){
        return SingletonFactory.singleton;
    }

    //序列化一直
    public Object readResolve(){
        return getInstance();
    }

    public static void main(String[] args) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.163.com");
        javaMailSender.setPort(25);
        javaMailSender.setUsername("m13302199872@163.com");
        javaMailSender.setPassword("xl41310251");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.setFrom("m13302199872@163.com");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo("xue.lei@aliyun.com");
            //mimeMessageHelper.setCc("m13302199872@163.com");
            FileInputStream fileInputStream = new FileInputStream("F:\\个人资料\\天津市电子税务局、“天津税务”手机APP缴纳灵活就业人员社会保险费操作指南.docx");
            mimeMessageHelper.setText("业人员社会保险费");
            mimeMessageHelper.setSubject("业人员社会保险费");
            //mimeMessageHelper.addAttachment("天津市电子税务局、“天津税务”手机APP缴纳灵活就业人员社会保险费操作指南.docx",new ByteArrayResource(IoUtil.readBytes(fileInputStream)));
            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(fileInputStream, MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
            mimeMessageHelper.addAttachment("天津市电子税务局、“天津税务”手机APP缴纳灵活就业人员社会保险费操作指南.docx", byteArrayDataSource);
            mimeMessageHelper.addAttachment("",new InputStreamResource(fileInputStream));
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
