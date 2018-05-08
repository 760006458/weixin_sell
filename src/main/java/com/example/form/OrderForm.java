package com.example.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xuan
 * @create 2018-04-07 16:34
 **/
@Data
public class OrderForm {
    @NotEmpty(message = "name不能为空")
    private String name;
    @NotEmpty(message = "phone不能为空")
    private String phone;
    @NotEmpty(message = "address不能为空")
    private String address;
    @NotEmpty(message = "openid不能为空")
    private String openid;
    @NotEmpty(message = "items不能为空")
    private String items;
}
