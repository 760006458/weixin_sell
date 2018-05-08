package com.example.repository;

import com.example.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author xuan
 * @create 2018-04-04 21:03
 **/
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    List<ProductInfo> findByProductStatus(Integer status);

    /**
     * 默认是jpql语句，@Modifying在修改和删除时不能省略
     * @param productId
     * @param quantity
     */
    @Query("update ProductInfo set productStock = productStock + ?2 where productId = ?1")
    @Modifying
    void updateProductInfoIncrStock(String productId, Integer quantity);
}
