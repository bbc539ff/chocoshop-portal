package com.chocoshop.service;

import com.chocoshop.mapper.OrderMapper;
import com.chocoshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    public List<Order> showAllOrders(){
        return orderMapper.selectAll();
    }

    public int countOrder(){
        return orderMapper.selectCount(new Order());
    }

    public int addOrder(Order order){
        try {
            order.setOrderUuid(UUID.randomUUID().toString());
            order.setOrderCreateTime(new Date());
            order.setOrderUpdateTime(new Date());
            System.out.println(order);
            return orderMapper.insert(order);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteOrder(Order order){
        return orderMapper.delete(order);
    }

    public int updateOrder(Order order){
        order.setOrderUpdateTime(new Date());
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public List<Order> search(Order order){
        try {
            return orderMapper.search(order);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> find(Order order) {
        return orderMapper.select(order);
    }

    public Order findByUuid(String orderUuid) {
        return orderMapper.selectByPrimaryKey(orderUuid);
    }

}
