package com.xuelei.tools;

import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.core.SaxExcelReader;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import fr.opensagres.poi.xwpf.converter.core.openxmlformats.ZipArchive;
import fr.opensagres.poi.xwpf.converter.pdf.FastPdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class ToolsApplicationTests {

    @Test
    void contextLoads() {
    }
    public void test0(){
        PdfConverter.getInstance();
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bytes = new byte[10];
            FastPdfConverter.getInstance().convert(ZipArchive.readZip(new ByteArrayInputStream(bytes)),
                    byteOutputStream, PdfOptions.create());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test1(){
        SaxExcelReader.of(null).readThen(new ByteArrayInputStream(null),(k,v)->{
        });

    }


}
