package com.xuelei.tools.pay.controller;


import cn.hutool.json.JSONUtil;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.xuelei.tools.pay.util.AliyunPayUtil;
import com.xuelei.tools.pay.util.QRUtil;
import com.xuelei.tools.pay.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("pay")
public class AlipayController {

    private final static Logger log = LoggerFactory.getLogger(AlipayController.class);

    @GetMapping("alipayByQrCodeBase64")
    public Mono<Object> alipayByQrCodeBase64(OrderVO orderVO) {

        AlipayTradePrecreateResponse response = null;
        try {
            response = AliyunPayUtil.payByQrCode(orderVO);
        } catch (Exception e) {
            log.error("支付失败",e);
            return Mono.just(ResponseEntity.ok().body("支付失败"));
        }
        log.info("支付信息：{}",JSONUtil.parse(response));
        return Mono.just(ResponseEntity.ok().body(QRUtil.generateQRCodeByBase64(response.getQrCode())));
    }

    @GetMapping("alipayByQrCodeStream")
    public Mono<ResponseEntity> alipayByQrCodeStream(OrderVO orderVO) {

        AlipayTradePrecreateResponse response = null;
        try {
            response = AliyunPayUtil.payByQrCode(orderVO);
        } catch (Exception e) {
            log.error("支付失败",e);
            return Mono.just(ResponseEntity.ok().body("支付失败"));
        }
        log.info("支付信息：{}",JSONUtil.parse(response));
        return Mono.just(ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header("Content-Disposition", "attachment; filename=myimage.png")
                .body(QRUtil.generateQRCodeByResponse(response.getQrCode())));
    }

    @GetMapping("alipayByPage")
    public Mono<ResponseEntity> alipayByPage(OrderVO orderVO) {

        AlipayTradePagePayResponse response = null;
        try {
            response = AliyunPayUtil.payByPage(orderVO);
        } catch (Exception e) {
            log.error("支付失败",e);
            return Mono.just(ResponseEntity.ok().body("支付失败"));
        }
        log.info("支付信息：{}",JSONUtil.parse(response));
        return Mono.just(ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
                .header("charset","utf-8")
                .body(response.getBody()));
    }

    @GetMapping("alipayByBarCodeStream")
    public Mono<ResponseEntity> alipayByBarCodeStream(OrderVO orderVO) {

        AlipayTradePayResponse response = null;
        try {
            response = AliyunPayUtil.payByBarCode(orderVO);
        } catch (Exception e) {
            log.error("支付失败",e);
            return Mono.just(ResponseEntity.ok().body("支付失败"));
        }
        log.info("支付信息：{}",JSONUtil.parse(response));

        return Mono.just(ResponseEntity.ok().body(JSONUtil.parse(response)));
//        return Mono.just(ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
//                .header("Content-Disposition", "attachment; filename=myimage.png")
//                .body(QRUtil.generateQRCodeByResponse(response.getQrCode())));
    }
}
