package com.example.pay.qrcode.impl;

import com.example.pay.qrcode.WeixinPayService;
import com.example.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuan
 * @create 2018-05-08 19:46
 **/
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${qrcode.appid}")
    private String appid;
    @Value("${qrcode.partner}")
    private String partner;
    @Value("${qrcode.partnerkey}")
    private String partnerkey;
    @Value("${qrcode.notifyurl}")
    private String notifyurl;

    /**
     * 生成二维码
     *
     * @param out_trade_no 订单号
     * @param total_fee    金额(分)
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative(String out_trade_no, String total_fee) {
        //1.创建参数
        Map<String, String> param = new HashMap();
        param.put("appid", appid);//公众号
        param.put("mch_id", partner);//商户号
        param.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
        param.put("body", "品优购");//商品描述
        param.put("out_trade_no", out_trade_no);//商户订单号
        param.put("total_fee", total_fee);//总金额（分）
        param.put("spbill_create_ip", "127.0.0.1");//IP
        param.put("notify_url", notifyurl);//回调地址
        param.put("trade_type", "NATIVE");//交易类型
        param.put("product_id", "1");//商品ID

        try {
            //2.生成要发送的xml
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println(xmlParam);
            //此HTTPClient是自己封装的工具类。用户下单后，调用微信的统一下单API生成预支付交易
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            //3.获得结果
            String result = client.getContent();
            System.out.println(result);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            Map<String, String> map = new HashMap<>();
            map.put("code_url", resultMap.get("code_url"));//支付地址
            map.put("total_fee", total_fee);//总金额
            map.put("out_trade_no", out_trade_no);//订单号
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        Map param = new HashMap();
        param.put("appid", appid);  //公众账号ID
        param.put("mch_id", partner);   //商户号
        param.put("out_trade_no", out_trade_no);    //订单号
        param.put("nonce_str", WXPayUtil.generateNonceStr());   //随机字符串
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String result = client.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}