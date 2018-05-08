package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xuan
 * @create 2018-04-09 13:09
 **/
@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WeChatAccountConfig {
    private String appID;
    private String appsecret;
    /**商户号*/
    private String mchId;
    /**商户秘钥*/
    private String mchKey;
    /**商户证书路径*/
    private String keyPath;
    /**微信支付异步通知地址*/
    private String notifyUrl;
}
