package com.example.service;

import com.example.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author xuan
 * @create 2018-04-06 14:52
 **/
public interface OrderMasterService {
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 根据订单ID查询订单时，需要级联将OrderDetail查出来
     *
     * @param orderId
     * @return
     */
    OrderDTO findOne(String orderId);

    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    Page<OrderDTO> findList(Pageable pageable);

    /**
     * 取消订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO cancle(OrderDTO orderDTO);

    OrderDTO finish(OrderDTO orderDTO);

    OrderDTO paid(OrderDTO orderDTO);

}
