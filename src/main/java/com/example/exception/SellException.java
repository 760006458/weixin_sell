package com.example.exception;

import com.example.enums.SellExceptionEnum;

/**
 * @author xuan
 * @create 2018-04-06 16:02
 **/
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(SellExceptionEnum sellExceptionEnum) {
        super(sellExceptionEnum.getMsg());
        this.code = sellExceptionEnum.getCode();
    }
}
