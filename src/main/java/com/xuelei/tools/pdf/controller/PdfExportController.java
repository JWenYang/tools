package com.xuelei.tools.pdf.controller;

import com.xuelei.tools.pdf.util.PDFUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;

/**
 * pdf export related
 */
@RestController
@RequestMapping("/pdf")
public class PdfExportController {

    private static Logger log = LoggerFactory.getLogger(PdfExportController.class);

    @GetMapping("/export")
    public Mono<ResponseEntity> export(){
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("createTime",new Date());
        String htmlStr = PDFUtil.freemarkerRender(dataMap, "templates/DataChart.ftl");
        log.info(htmlStr);
        byte[] pdf = PDFUtil.createPdf(htmlStr, "templates/font/SIMSUN.TTC");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment","test.pdf");
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        return Mono.just(new ResponseEntity<>(pdf, httpHeaders, HttpStatus.OK));
    }
}
