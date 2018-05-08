package com.example.dto;

import com.example.entity.OrderDetail;
import com.example.enums.OrderStatusEnum;
import com.example.enums.PayStatusEnum;
import com.example.serialize.Date2LongSerialize;
import com.example.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Date Transfer Object用于封装传输的数据，不跟数据库打交道
 * @author xuan
 * @create 2018-04-06 14:53
 **/
@Data
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
    @JsonSerialize(using = Date2LongSerialize.class)
    private Date createTime;
    @JsonSerialize(using = Date2LongSerialize.class)
    private Date updateTime;
    /**该字段数据库没有，然而并没有使用@Transient添加到OrderMaster中*/
    private List<OrderDetail> orderDetails;

    @JsonIgnore
    public String getOrderStatusByCode(){
        return EnumUtil.getByCode(this.orderStatus, OrderStatusEnum.class).getMsg();
    }

    @JsonIgnore
    public String getPayStatusByCode(){
        return EnumUtil.getByCode(this.payStatus, PayStatusEnum.class).getMsg();
    }
}
