package com.example.dto;

import lombok.Data;

/**
 * @author xuan
 * @create 2018-04-06 14:35
 **/
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO() {
    }

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
