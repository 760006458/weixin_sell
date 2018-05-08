package com.example.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * DynamicUpdate注解：针对数据库的updateTime自动更新字段（数据库根据数据自动更新）
 * 但是如果数据查出来没有修改，那么这个字段是不会被数据库实时更新的
 * @author DELL
 * @create 2018-04-04 15:36
 **/
@Entity //不加Entity注解报错：Not a managed type
@DynamicUpdate
@Data
public class ProductCategory {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    默认是AUTO，会根据数据库自动选择，本初是mysql，所以就是IDENTITY
    @GeneratedValue
    private Integer catagoryId;
    private String catagoryName;//catagory_name,catagory_Name均可由jpa完成自动封装
    private Integer catagoryType;
    private Date createTime;
    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String catagoryName, Integer catagoryType) {
        this.catagoryName = catagoryName;
        this.catagoryType = catagoryType;
    }
}
