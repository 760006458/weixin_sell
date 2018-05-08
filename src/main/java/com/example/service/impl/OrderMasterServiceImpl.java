package com.example.service.impl;

import com.example.dto.OrderDTO;
import com.example.entity.OrderDetail;
import com.example.entity.OrderMaster;
import com.example.entity.ProductInfo;
import com.example.enums.OrderStatusEnum;
import com.example.enums.PayStatusEnum;
import com.example.enums.SellExceptionEnum;
import com.example.exception.SellException;
import com.example.repository.OrderDetailRepository;
import com.example.repository.OrderMasterRepository;
import com.example.repository.ProductInfoRepository;
import com.example.service.OrderMasterService;
import com.example.service.ProductInfoService;
import com.example.util.KeyUtil;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuan
 * @create 2018-04-06 15:01
 **/
@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal totalCount = new BigDecimal(0);
        String orderId = KeyUtil.genOrderId();
        //查询商品（单价，库存）
        for (OrderDetail orderDetail : orderDTO.getOrderDetails()) {
            ProductInfo productInfo = productInfoRepository.findOne(orderDetail.getProductId());
            //扣库存
            productInfoService.decreaseStock(productInfo, orderDetail.getProductQuantity());
            //计入总价
            totalCount = totalCount.add(productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())));
            //保存OrderDetail
            orderDetail.setDetailId(KeyUtil.genOrderId());
            orderDetail.setOrderId(orderId);
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductIcon(productInfo.getProductIcon());
            orderDetail.setProductPrice(productInfo.getProductPrice());
            orderDetailRepository.save(orderDetail);
        }
        //保存OrderMaster
        orderDTO.setOrderId(orderId);
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(totalCount);
//        orderMaster.setOrderId(orderId);
//        orderMaster.setBuyerName(orderDTO.getBuyerName());
//        orderMaster.setBuyerAddress(orderDTO.getBuyerAddress());
//        orderMaster.setBuyerPhone(orderDTO.getBuyerPhone());
//        orderMaster.setBuyerOpenid(orderDTO.getBuyerOpenid());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        if (orderId == null) {
            log.error("【查询订单】订单号不存在，orderId={}", orderId);
            throw new SellException(SellExceptionEnum.ORDER_ID_NOT_EXIST);
        }
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null) {
            log.error("【查询订单】订单不存在，orderId={}", orderId);
            throw new SellException(SellExceptionEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        //TODO CollectionUtils
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.error("【查询订单】订单详情不存在，orderId={}", orderId);
            throw new SellException(SellExceptionEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetails(orderDetailList);
        return orderDTO;
    }

    /**
     * 带条件分页查询订单主表信息，不要详情表信息
     *
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> list = page.getContent().stream().map(e -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(e, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findAll(pageable);
        List<OrderDTO> list = page.getContent().stream().map(e -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(e, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancle(OrderDTO orderDTO) {
        //判断订单状态：只有新订单可以取消，订单支付完成了，就不能取消订单了
        if (orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()) {
            log.error("【取消订单】订单状态不对，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCLE.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateStatus = orderMasterRepository.save(orderMaster);
        if (updateStatus == null) {
            log.error("【取消订单】订单更新失败，orderMaster={}", orderMaster);
            throw new SellException(SellExceptionEnum.ORDER_UPDATE_FAIL);
        }
        //返还库存
        for (OrderDetail orderDetail : orderDTO.getOrderDetails()) {
            productInfoService.increaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
        }
        //TODO 如果支付了，还需要退钱（支付成功就到不了这一步呀，奇怪）
        return orderDTO;
    }

    /**
     * 订单状态是新订单
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()) {
            log.error("【完结订单】订单状态错误，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null) {
            log.error("【完结订单】订单更新失败，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    /**
     * 新订单且等待支付状态，才可以完成支付
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        if (orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()) {
            log.error("【订单支付】订单状态错误，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_STATUS_ERROR);
        }
        if (orderDTO.getPayStatus() != PayStatusEnum.WAIT.getCode()) {
            log.error("【订单支付】支付状态错误，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_PAY_STATUS_ERROR);
        }
        //注：此处只是支付完结，订单还没有完结
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null) {    //保存失败
            log.error("【订单支付】订单支付失败，orderDTO={}", orderDTO);
            throw new SellException(SellExceptionEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
