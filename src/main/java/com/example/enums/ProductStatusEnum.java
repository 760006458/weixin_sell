package com.example.enums;

import lombok.Getter;

/**
 * @author xuan
 * @create 2018-04-04 21:57
 **/
@Getter
public enum ProductStatusEnum {
    UP(0,"上架"),DOWN(1,"下架");

    private int code;
    private String message;

    ProductStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
