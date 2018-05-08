package com.example.service.impl;

import com.example.entity.ProductCategory;
import com.example.enums.ProductStatusEnum;
import com.example.repository.ProductCategoryRepository;
import com.example.service.ProductCategoryService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author DELL
 * @create 2018-04-04 17:10
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryIn(Integer[] categorys) {
        return repository.findByCatagoryTypeIn(categorys);
    }

    @Override
    public void save(ProductCategory category) {
        repository.save(category);
    }

}
