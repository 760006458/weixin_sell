package com.example.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuan
 * @create 2018-04-04 20:58
 **/
@Entity
@DynamicUpdate
@Data
public class ProductInfo {
    @Id
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    /**商品图片路径*/
    private String productIcon;
    private Integer catagoryType;
    private Integer productStatus;
    private Date createTime;
    private Date updateTime;
}
