package com.example.service;

import com.example.entity.ProductCategory;

import java.util.List;

/**
 * @author DELL
 * @create 2018-04-04 17:07
 **/
public interface ProductCategoryService {
    ProductCategory findOne(Integer id);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryIn(Integer[] categorys);

    void save(ProductCategory category);
}
