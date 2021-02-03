package com.xuelei.tools.pdf.util;


import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

/**
 * pdf generate tools
 */
public class PDFUtil {

    private static Logger log = LoggerFactory.getLogger(PDFUtil.class);

    private volatile static Configuration configuration;

    static{
        if(configuration == null){
            synchronized(PDFUtil.class){
                configuration = new Configuration(Configuration.VERSION_2_3_19);
            }
        }
    }

    /**
     * rendering html
     * @return
     */
    public static String freemarkerRender(Map<String,Object> dataMap,String templatePath){
        StringWriter stringWriter = new StringWriter();
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            //获取模板
            File file = new ClassPathResource(templatePath).getFile();
            configuration.setDirectoryForTemplateLoading(file.getParentFile());
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            Template template = configuration.getTemplate(file.getName());
            template.process(dataMap,stringWriter);
            stringWriter.flush();
        } catch (Exception e) {
            log.error("freemaker render fail",e);
            return null;
        }
        return stringWriter.toString();
    }

    /**
     * create pdf
     * @return
     */
    public static byte[] createPdf(String htmlStr,String fontFile){
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ITextRenderer iTextRenderer = new ITextRenderer();
            iTextRenderer.setDocumentFromString(htmlStr);
            //设置字体，解决中文乱码问题
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            fontResolver.addFont(new ClassPathResource(fontFile).getPath(),
                    BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            iTextRenderer.layout();
            iTextRenderer.createPDF(outputStream);
            iTextRenderer.finishPDF();
            result = outputStream.toByteArray();
            outputStream.flush();;
            outputStream.close();
        } catch (Exception e) {
            log.error("htmlstr to pdf fail",e);
            return null;
        }
        return result;
    }
}
