package com.example.repository;

import com.example.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author DELL
 * @create 2018-04-04 15:39
 **/
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    List<ProductCategory> findByCatagoryTypeIn(Integer[] categorys);
}
