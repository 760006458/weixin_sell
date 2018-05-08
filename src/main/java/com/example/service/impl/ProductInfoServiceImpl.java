package com.example.service.impl;

import com.example.entity.ProductInfo;
import com.example.enums.ProductStatusEnum;
import com.example.enums.SellExceptionEnum;
import com.example.exception.SellException;
import com.example.repository.ProductInfoRepository;
import com.example.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuan
 * @create 2018-04-04 21:08
 **/
@Service
@Transactional
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String id) {
        return productInfoRepository.findOne(id);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public void save(ProductInfo productInfo) {
        productInfoRepository.saveAndFlush(productInfo);
    }

    @Override
    public void increaseStock(String productId, Integer incr) {
        try {
            productInfoRepository.updateProductInfoIncrStock(productId, incr);
        } catch (Exception e) {
            log.error("【增加库存】增加库存失败，productId={},incr={}", productId, incr);
            throw new SellException(SellExceptionEnum.PRODECT_UPDATE_FAIL);
        }
    }

    @Override
    public void decreaseStock(ProductInfo productInfo, Integer decr) {
        //如果商品不存在就抛异常
        if (productInfo == null) {
            log.error("【商品减库存】商品不存在，productInfo={}",productInfo);
            throw new SellException(SellExceptionEnum.PRODUCT_NOT_EXIST);
        }
        //如果库存足够就扣库存，否则抛异常
        //TODO  高并发下在查询商品和减库存间存在安全隐患
        if (productInfo.getProductStock() < decr) {
            throw new SellException(SellExceptionEnum.PRODUCT_STOCK_ERROR);
        }
        productInfo.setProductStock(productInfo.getProductStock() - decr);
        productInfoRepository.save(productInfo);
    }

}
