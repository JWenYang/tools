package com.xuelei.tools.pay.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class QRUtil {

    private final static Logger log = LoggerFactory.getLogger(QRUtil.class);

    private final static int WIDTH = 300;
    private final static int HEIGHT = 300;
    private final static String IMAGE_FORMAT = "png";
    private final static String PREFIX = new StringBuilder("data:image/").append(IMAGE_FORMAT).append(";base64,").toString();

    /**
     * 推入响应流
     * @param text
     * @param text
     */
    public static byte[] generateQRCodeByResponse(String text){
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            generateQRCode(text,byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败",e);
        }
    }

    /**
     * String of Base64
     * @param text
     * @return
     */
    public static String generateQRCodeByBase64(String text){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        generateQRCode(text, byteArrayOutputStream);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PREFIX);
        stringBuilder.append(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
        try {
            if(null != byteArrayOutputStream) {
                byteArrayOutputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("生成二维码失败",e);
        }
        return stringBuilder.toString();
    }

    private static void generateQRCode(String text,OutputStream outputStream){
        //创建二维码生成器
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //生成二维码
        try {
            //生成
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            //采用base64进行编码
            MatrixToImageWriter.writeToStream(bitMatrix,IMAGE_FORMAT,outputStream);
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败",e);
        }
    }
}
