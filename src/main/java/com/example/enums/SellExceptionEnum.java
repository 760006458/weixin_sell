package com.example.enums;

import lombok.Getter;

/**
 * @author xuan
 * @create 2018-04-06 16:05
 **/
@Getter
public enum SellExceptionEnum {
    PARAM_ERROR(1, "参数不合法"),
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_STOCK_ERROR(11, "库存不足"),
    ORDER_ID_NOT_EXIST(12, "订单号不存在"),
    ORDER_NOT_EXIST(13, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(14, "订单详情不存在"),
    ORDER_STATUS_ERROR(15, "订单状态错误"),
    ORDER_UPDATE_FAIL(16, "订单更新失败"),
    PRODECT_UPDATE_FAIL(17, "商品表修改失败"),
    ORDER_PAY_STATUS_ERROR(18, "订单支付状态错误"),
    ORDER_OWNNER_ERROR(19, "订单拥有者不一致");

    private Integer code;
    private String msg;

    SellExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
