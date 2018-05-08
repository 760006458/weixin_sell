package com.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.dto.OrderDTO;
import com.example.entity.OrderDetail;
import com.example.enums.SellExceptionEnum;
import com.example.exception.SellException;
import com.example.form.OrderForm;
import com.example.service.OrderMasterService;
import com.example.util.ResultVoUtil;
import com.example.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuan
 * @create 2018-04-07 16:31
 **/
@Controller
@RequestMapping("/buyer/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("/create")
    @ResponseBody
    public ResultVo create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不合法，orderForm={}", orderForm);
            throw new SellException(SellExceptionEnum.PARAM_ERROR);
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        try {
            List<OrderDetail> orderDetails = JSONArray.parseArray(orderForm.getItems(), OrderDetail.class);
            orderDTO.setOrderDetails(orderDetails);
        } catch (Exception e) {
            log.error("【创建订单】参数不合法，orderForm={}", orderForm);
            throw new SellException(SellExceptionEnum.PARAM_ERROR);
        }
        OrderDTO dto = orderMasterService.create(orderDTO);
        //"data": {"orderId": "1523096232760"} 可以直接使用HashMap
        HashMap<Object, Object> map = new HashMap<>();
        map.put("orderId", dto.getOrderId());
        return ResultVoUtil.success(map);
    }


    @GetMapping("/list")
    @ResponseBody
    public ResultVo findOrderOne(@RequestParam String openid,HttpServletRequest request,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {

        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid为空");
            throw new SellException(SellExceptionEnum.PARAM_ERROR);
        }
        Pageable pageable = new PageRequest(page, size);
        Page<OrderDTO> list = orderMasterService.findList(openid, pageable);

        return ResultVoUtil.success(list.getContent());
    }

    @GetMapping("/detail")
    @ResponseBody
    public ResultVo orderDetail(@RequestParam String openid,
                                @RequestParam String orderId) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单详情】openid为空");
            throw new SellException(SellExceptionEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = orderOwnerValidate(openid, orderId);
        //滤除OrderDetail中的createTime和updateTime,API文档要求
        orderDTO.getOrderDetails().stream().map(e -> {
            e.setCreateTime(null);
            e.setUpdateTime(null);
            return e;
        }).collect(Collectors.toList());
        return ResultVoUtil.success(orderDTO);
    }

    /**
     * 注意：查询和取消订单需要权限，否则别人可以随便查看和取消你的订单（只需要提供自己的openid)
     * 查询没问题，但是要根据查询结果中的openid跟页面传过来的openid比对，一致说明是一个人OK；不一致说明是两个人，要抛异常
     * @param openid
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public ResultVo orderCancel(@RequestParam String openid,
                                @RequestParam String orderId) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单详情】openid为空");
            throw new SellException(SellExceptionEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = orderOwnerValidate(openid, orderId);
        orderMasterService.cancle(orderDTO);
        return ResultVoUtil.success();
    }

    private OrderDTO orderOwnerValidate(String openid, String orderId) {
        OrderDTO orderDTO = orderMasterService.findOne(orderId);
        if (orderDTO == null) {
            log.error("【取消订单】订单不存在，orderId={}", orderId);
            throw new SellException(SellExceptionEnum.ORDER_NOT_EXIST);
        }
        if (!openid.equals(orderDTO.getBuyerOpenid())) {
            log.error("【查询订单】订单的拥有者不一致，openid={}，orderDTO={}", openid, orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_OWNNER_ERROR);
        }
        return orderDTO;
    }

}
