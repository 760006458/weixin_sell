package com.example.controller;

import com.example.entity.ProductCategory;
import com.example.entity.ProductInfo;
import com.example.service.ProductCategoryService;
import com.example.service.ProductInfoService;
import com.example.util.ResultVoUtil;
import com.example.vo.CategoryVo;
import com.example.vo.ProductVo;
import com.example.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xuan
 * @create 2018-04-04 21:55
 **/
@RestController
@RequestMapping("/buyer/product")
public class ProductController {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ProductCategoryService categoryService;

    /**
     * 由于涉及jpa结果集的嵌套封装问题，jpa貌似没有提供类似hibernate和mybatis的嵌套封装方式。
     * 由于商品种类可能很多，所以不应该遍历Category去查询数据库，这样和数据库交互太多，
     * 所以此处先将所有商品和所有分类查出来（两次交互），然后双层for循环嵌套封装VO
     * 数据格式：Result --> List[Category --> List[ProductInfo]]
     *
     * @return
     */
    @GetMapping("/list")
    public ResultVo list() {
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        //此处的目的是为了防止很多分类可能没有商品，那么会增加很多没意义的双层for循环的次数
        List<Integer> categoryIdList = productInfoList.stream().map(e -> e.getCatagoryType()).distinct().collect(Collectors.toList());
        List<ProductCategory> categoryList = categoryService.findByCategoryIn(categoryIdList.toArray(new Integer[0]));

        ArrayList<CategoryVo> categoryVoList = new ArrayList<>();
        for (ProductCategory category : categoryList) { //外层遍历目录
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCatagoryType(category.getCatagoryType());
            categoryVo.setCategoryName(category.getCatagoryName());
            categoryVoList.add(categoryVo);

            ArrayList<ProductVo> productVoList = new ArrayList<>();
            categoryVo.setFoods(productVoList);
            for (ProductInfo productInfo : productInfoList) {   //内层遍历商品
                if (productInfo.getCatagoryType() == category.getCatagoryId()) {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(productInfo,productVo);
                    productVoList.add(productVo);
                }
            }
        }

        return ResultVoUtil.success(categoryVoList);
    }
}
