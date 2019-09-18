package com.chocoshop.mapper;

import com.chocoshop.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends tk.mybatis.mapper.common.Mapper<Order> {

    @Select("<script>" +
            "SELECT * FROM cc_order " +
            "<where>" +
            "<if test=\"orderUuid != null\">" +
            "order_uuid = #{orderUuid}" +
            "</if>" +
            "<if test=\"orderPayment != null\">" +
            "AND order_payment = #{orderPayment}" +
            "</if>" +
            "<if test=\"orderPaymentType != null\">" +
            "AND order_payment_type = #{orderPaymentType}" +
            "</if>" +
            "<if test=\"orderStatus != null\">" +
            "order_status = #{orderStatus}" +
            "</if>" +
            "<if test=\"orderCreateTime != null\">" +
            "AND DATE_FORMAT(order_create_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderCreateTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderUpdateTime != null\">" +
            "AND DATE_FORMAT(order_update_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderUpdateTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderPaymentTime != null\">" +
            "AND DATE_FORMAT(order_payment_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderPaymentTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderConsignTime != null\">" +
            "AND DATE_FORMAT(order_consign_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderConsignTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderEndTime != null\">" +
            "AND DATE_FORMAT(order_end_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderEndTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderCloseTime != null\">" +
            "AND DATE_FORMAT(order_close_time, '%Y-%m-%d') =  DATE_FORMAT(#{orderCloseTime}, '%Y-%m-%d')" +
            "</if>" +
            "<if test=\"orderShippingCode != null\">" +
            "AND order_shipping_code = #{orderShippingCode}" +
            "</if>" +
            "<if test=\"memberUuid != null\">" +
            "AND member_uuid = #{memberUuid}" +
            "</if>" +
            "<if test=\"orderGoodsList != null\">" +
            "AND order_goods_list LIKE CONCAT('%, ', #{orderGoodsList}, '/%')" +
            "</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "orderUuid", column = "order_uuid"),
            @Result(property = "orderPayment", column = "order_payment"),
            @Result(property = "orderPaymentType", column = "order_payment_type"),
            @Result(property = "orderStatus", column = "order_status"),
            @Result(property = "orderCreateTime", column = "order_create_time"),
            @Result(property = "orderUpdateTime", column = "order_update_time"),
            @Result(property = "orderPaymentTime", column = "order_payment_time"),
            @Result(property = "orderConsignTime", column = "order_consign_time"),
            @Result(property = "orderEndTime", column = "order_end_time"),
            @Result(property = "orderCloseTime", column = "order_close_time"),
            @Result(property = "orderShippingCode", column = "order_shipping_code"),
            @Result(property = "memberUuid", column = "member_uuid"),
            @Result(property = "orderGoodsList", column = "order_goods_list"),
    })
    List<Order> search(Order order);
}
