package com.example.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuan
 * @create 2018-04-04 21:47
 **/
@Data
public class CategoryVo {
    @JsonProperty("name")
    private String categoryName;//发现categoryName首字母大写了，然后json中同时出现categoryName和name字段
    @JsonProperty("type")
    private Integer catagoryType;
    private List<ProductVo> foods;
}
