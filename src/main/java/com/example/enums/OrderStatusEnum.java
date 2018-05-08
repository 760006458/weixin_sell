package com.example.enums;

import lombok.Getter;

/**
 * @author xuan
 * @create 2018-04-06 15:05
 **/
@Getter
public enum OrderStatusEnum implements CodeEnum{
    NEW(0, "新订单"), FINISHED(1, "完结"), CANCLE(2, "已取消");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
