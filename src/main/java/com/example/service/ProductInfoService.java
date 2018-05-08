package com.example.service;

import com.example.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author xuan
 * @create 2018-04-04 21:04
 **/
public interface ProductInfoService {
    ProductInfo findOne(String id);

    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    void save(ProductInfo productInfo);

    /**
     * TODO
     * 增减库存方法形参没有统一：减库存调用了jpa自带的save方法，加库存是自定义的修改方法
     * @param productId
     * @param incr
     */
    void increaseStock(String productId, Integer incr);

    void decreaseStock(ProductInfo productInfo, Integer decr);
}
