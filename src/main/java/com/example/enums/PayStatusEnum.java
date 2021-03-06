package com.example.enums;

import lombok.Getter;

/**
 * @author xuan
 * @create 2018-04-06 15:08
 **/
@Getter
public enum PayStatusEnum implements CodeEnum {
    WAIT(0, "等待支付"),
    SUCCESS(1, "支付完成"),
    TIMEOUT(2, "支付超时"),
    FAIL(3, "支付失败");

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
