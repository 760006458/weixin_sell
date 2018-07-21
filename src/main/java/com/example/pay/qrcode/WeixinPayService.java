package com.example.pay.qrcode;

import java.util.Map;

/**
 * @author xuan
 * @create 2018-05-08 19:45
 **/
public interface WeixinPayService {

    /**
     * 调用微信API，生成微信支付的路径，返回前台，由前台生成二维码
     *
     * @param out_trade_no 订单号
     * @param total_fee    金额(分)
     * @return
     */
    Map createNative(String out_trade_no, String total_fee);

    /**
     * 调用微信API，查询订单状态
     * @param out_trade_no
     * @return
     */
    Map queryPayStatus(String out_trade_no);
}
