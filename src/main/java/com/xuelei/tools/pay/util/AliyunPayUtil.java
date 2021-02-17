package com.xuelei.tools.pay.util;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.xuelei.tools.pay.vo.OrderVO;

public class AliyunPayUtil {

    public static AlipayTradePrecreateResponse payByQrCode(OrderVO orderVO) throws Exception {
        //1、收款二维码
        return Factory.Payment.FaceToFace()
                    .preCreate(orderVO.getName(), orderVO.getOrderNo(), orderVO.getPrice().toString());
    }


    public static AlipayTradePagePayResponse payByPage(OrderVO orderVO) throws Exception {
        //1、网站跳转
        return Factory.Payment.Page()
                .pay(orderVO.getName(), orderVO.getOrderNo(), orderVO.getPrice().toString(),"https://it.yusys.com.cn/yusys/main.asp");
    }

    public static AlipayTradePayResponse payByBarCode(OrderVO orderVO) throws Exception {
        //1、扫描条形码
        return Factory.Payment.FaceToFace()
                .pay(orderVO.getName(), orderVO.getOrderNo(), orderVO.getPrice().toString(),"1111");
    }

}
