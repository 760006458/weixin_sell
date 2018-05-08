package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 使用第三方SDK对接微信接口
 *
 * @author xuan
 * @create 2018-04-09 13:23
 **/
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatThreeSDKController {

    @Value("${project.domain}")
    private String domain;

    @Autowired
    private WxMpService wxMpService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam String returnUrl) {
        String url = domain + "/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, returnUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam String code, @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
        }
        log.info("【微信网页授权】access_token={}", accessToken);
        String openId = accessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    public static void main(String[] args) {
        try {
            System.out.println(URLEncoder.encode("http://uwe2er.natappfree.cc", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
