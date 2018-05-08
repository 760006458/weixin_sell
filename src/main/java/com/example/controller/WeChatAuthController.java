package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.util.Decript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 直接跟微信接口对接
 * @author xuan
 * @create 2018-04-08 15:06
 **/
@RequestMapping("/wechat")
@RestController
@Slf4j
public class WeChatAuthController {

    //token
    private final String token = "weixin_sell";
    private final String APPID = "wxdd055816d2869569";
    private final String APPSECRET = "337a75b1adb73bc395d74a06468ac699";


    @GetMapping("/auth")
    public void auth(@RequestParam String signature,
                     @RequestParam String timestamp,
                     @RequestParam String nonce,
                     @RequestParam String echostr,
                     HttpServletResponse response) {

        ArrayList<String> array = new ArrayList<String>();
        array.add(signature);
        array.add(timestamp);
        array.add(nonce);

        //自然排序并无缝拼接
        String sortString = sortAndAppend(token, timestamp, nonce);
        //SHA1加密
        String mytoken = Decript.SHA1(sortString);
        //校验签名
        if (!StringUtils.isEmpty(mytoken) && mytoken.equals(signature)) {
            log.info("【微信认证】签名校验通过");
            try {
                //如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
                response.getWriter().println(echostr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info("【微信认证】签名校验失败");
        }
    }

    @RequestMapping("/auth_code")
    public void auth_code(@RequestParam String code) {
        log.info("【获取code】,code={}", code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID
                + "&secret=" + APPSECRET + "&code="+ code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        HashMap hashMap = JSON.parseObject(response, HashMap.class);
        log.info("【获取openid】，openid={}", hashMap.get("openid"));
    }


    public static String sortAndAppend(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }
        return sbuilder.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("http://uvyjex.natappfree.cc/sell/wechat/auth_code", "utf-8"));
    }

}

