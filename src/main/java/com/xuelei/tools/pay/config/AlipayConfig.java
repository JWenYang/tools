package com.xuelei.tools.pay.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    private final static Logger log = LoggerFactory.getLogger(AlipayConfig.class);

    @Bean
    public void setAliyunConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = "2016101500691575";

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDpy4fOvwhU/2YhOoLCn7spevvUOX+iX7T2ZoKFhNo7/lmEtKX0E3Qsv2OGNgH7WszXuazMBHnJfMnlqCQejWb5/XRnpQ+BXVREhCczvJkhRNmTFM0ZW4TOrlgQJvsdSLZg6YkEUKv2rrBZkyxA2UL3HSgq+ETTr7u8wZNc49Rwmlj3eXmCrqmDkmryqyuuBfNzKf6wAm/PdX3/7qTo2uMErbI8rr7xXnHT+SgV/v8ihABjwYbHPMyeDlrrUqnSLzUedEckBhCXUa3EsCpTCWkkfha4yZQave0Iy313b4NfV+50OWwXUD3uGjXRO4q/c/puzu9KqGUurSJXOvndnVTBAgMBAAECggEAeQitHhM3tQkjTkCEQU2AD1Fj3dKGiQETJhybpZPJhxV+Mn1zDJClrCTI2UpMbFvtgCDXnCcVBYJc40/QoyCBFxpnkOCazlYKsg3YXahdfiOun749D8uZTYZkgLgteFOUV0ePb3R0EhkNotHWLxxK1rN5gAVXXF+1yVpNd+FasY+4y5YpVChG08xTb0LhjIH1V5mfjOtU0+DJ0QkZcGXY0jtfubrMB+LTGmlPnYSmS4kJxoVwuKAbNnF5jrRfRHAqK1xu4j3THB1DQ5FQ3RFxxvGAc8vdzsUc6cp2tk87p7pchL8eb/C2Q9A+XQGq+/qZNUw5L4JqRG23Io3BAOqwoQKBgQD+wOxaSoS2bpwurmUhUxoSruGDwgiPRMqJhcvU3C2wpah9V98VYUXzMaRkBKG4oDUtresa60ec8Kx4CW/mf0H+ZS7GMwR+v3C2I6NeU7qCJS4572DgN46wk/WiG88+eRdCHVXx7wwHvp1nXWnJM+l6xQJ/Qx1v4YXuKXhcGkQMYwKBgQDq8FtYfIsNIMTsrlgkrZeoUMGDwOxnKEs8iXI7XSVe27LqurgTyB8NO49NhgObf7eiPfZb8BzgT4ru5K2ZLzIosV/djGfYRbyiJg5FwwxkERgm32iwyf216k6E2gq8H9qgorzSHQuaxtRBleyeITMNwkPY6ZNV1KshdtK01PJpiwKBgCF09tV42esXpb1fu/pfbpori4SBVBx0vWDISJfA/uMnLnSfuLWXG9evmlZkYVTc/X7tS2YypioPVadCHgcYiXM+mKEBDEMVI1lYXtlBh8oV1fdX39IymbPJCsZqfpJG1u4g6ium6F6QAx4TH5SEE9LC0uwhxVjvNmar3ZnC/VLfAoGAK4X9liX0aKkychpWT+0s1EypnOvnn7CyR3O7Cs5Vtv4CZkPTCC8huYTYJmd2UZ9WeKL7+cpAvzldJqaMln1M0IcTmGqDbmh06qEcMcZRjFQyYjkvHtk8vC+8/hWauYE5MlsTmeUxminNdpz8qcrCt4DOqueSmOr+DQQDQubURcMCgYByuiTmg4mOnoz6yTTeRimWBALxmVpQY7M+uFUy6nFT1y74DnzDAa3ir+hn9Y2EizZ9tiPWiecZUwY7mscyOl+h742Dw1bEZu4Yj+Iww1wbQsg/4S93SOj6dUVnpF7fL7IR38URg6+ZTmcPIDX923l/Bk3RB3DlWAT3s+ZY++JUmg==";

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        //应用公钥证书文件
        config.merchantCertPath = "paysecret/alipay/appCertPublicKey_2016101500691575.crt";
        //支付宝公钥证书文件
        config.alipayCertPath = "paysecret/alipay/alipayCertPublicKey_RSA2.crt";
        //支付宝根证书文件
        config.alipayRootCertPath = "paysecret/alipay/alipayRootCert.crt";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        // config.alipayPublicKey = "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "";
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = "";
        //全局设置
        Factory.setOptions(config);
    }
}
