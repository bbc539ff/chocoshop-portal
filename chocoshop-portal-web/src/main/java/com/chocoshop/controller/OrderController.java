package com.chocoshop.controller;

import com.chocoshop.model.Goods;
import com.chocoshop.model.Member;
import com.chocoshop.model.Order;
import com.chocoshop.model.json.Cart;
import com.chocoshop.service.GoodsService;
import com.chocoshop.service.MemberService;
import com.chocoshop.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    CartController cartController;

    @Autowired
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(path = "/order/submit", method = RequestMethod.POST)
    public String generateOrder(@CookieValue String memberUuid, Order order, String checkedGoodsId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // cart转Cart对象
        Cart cart = cartController.cartDecode(request.getCookies());
        logger.info("cart: "+cart);
        // 获取goodsIdMap
        Map<Long, Integer> goodsIdMap = cart.getGoodsIdMap();
        // 把选中商品封装成Cart对象
        Cart orderCart = cart = new Cart(new HashMap<>(), 0, new BigDecimal(0));
        // 遍历选中的商品，更新商品库存;
        List<Long> checkedGoodsIdList = (List<Long>) objectMapper.readValue(URLDecoder.decode(checkedGoodsId, "utf-8"), new TypeReference<List<Long>>(){});
        for (Long goodsId : checkedGoodsIdList) {
            Goods goods = new Goods();
            goods.setGoodsId(goodsId);
            goods = goodsService.getGoodsById(goodsId);
            Integer newNumber = goods.getGoodsNumber()-goodsIdMap.get(goodsId);
            goods.setGoodsNumber(newNumber);
            goodsService.updateGoods(goods, null);

            orderCart.getGoodsIdMap().put(goodsId, goodsIdMap.get(goodsId));
            orderCart.setPrice(goods.getGoodsPrice().multiply(new BigDecimal(goodsIdMap.get(goodsId))));
            orderCart.setNumber(orderCart.getNumber()+1);
        }

        // 生成订单对象并添加
        order.setMemberUuid(memberUuid);
        order.setOrderStatus(0);

        String cartJSON = objectMapper.writeValueAsString(orderCart);
        String cookieContent = URLEncoder.encode(cartJSON, "utf-8");
        order.setOrderGoodsList(cookieContent);
        orderService.addOrder(order);

        // 从Cookie中删除下单商品
        Cart cartObject = cartController.multiDel(checkedGoodsIdList, request.getCookies());
        logger.info("generateOrder(): "+cartObject.toString());
        // Cart对象转JSON对象并URLEncode
        String cartObjectJSON = objectMapper.writeValueAsString(cartObject);
        String cartCookieContent = URLEncoder.encode(cartObjectJSON, "utf-8");
        // // 更新后的Cart的JSON写入Cookie
        Cookie cookie = new Cookie("cart", cartCookieContent);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/order/info?orderStatus=0";
    }
    @ExceptionHandler(Exception.class)
    public void myExceptionHandler(Exception e){
        e.printStackTrace();
    }

    @RequestMapping(path = "/order/info")
    public String userOrder(Model model, @RequestParam Integer orderStatus) {
        Member member = (Member) SecurityUtils.getSubject().getPrincipal();
        Order order = new Order();
        if (!Objects.equals(orderStatus, -1)) {
            order.setOrderStatus(orderStatus);
        }
        order.setMemberUuid(member.getMemberUuid());
        List<Order> orderList = orderService.search(order);

        model.addAttribute("orderList", orderList);
        return "order/order_info";
    }


    @RequestMapping(path = "/order/detail/{orderUuid}")
    public String orderDetail(Model model, @PathVariable String orderUuid, HttpServletRequest request) {
        try {
            Order order = orderService.findByUuid(orderUuid);

            // Order中取出商品Id列表
            String cartJSON = order.getOrderGoodsList();
            Cart cart = (Cart) objectMapper.readValue(URLDecoder.decode(cartJSON, "utf-8"), Cart.class);
            Map<Long, Integer> goodsIdMap = cart.getGoodsIdMap();
            try {
                // 遍历通过goodsId获取Goods对象存到goodsMap
                LinkedHashMap<Goods, Integer> goodsMap = new LinkedHashMap<>();
                for (Map.Entry<Long, Integer> entry : goodsIdMap.entrySet()) {
                    Long goodsId = entry.getKey();
                    Integer num = entry.getValue();
                    Goods goods = goodsService.getGoodsById(goodsId);
                    goodsMap.put(goods, num);
                }
                model.addAttribute("goodsMap", goodsMap);

                String username = ((Member)SecurityUtils.getSubject().getPrincipal()).getMemberUserName();
                Member member = memberService.findByMemberName(username);

                model.addAttribute("member", member);
                model.addAttribute("cartMap", goodsMap);
                model.addAttribute("order", order);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "order/order_detail";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "403";
    }

    @RequestMapping(path = "/order/consign/{orderUuid}")
    public String consign(@PathVariable String orderUuid) {
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(3);
        order.setOrderConsignTime(new Date());
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=-1";
    }

    @RequestMapping(path = "/order/pay/{orderUuid}")
    public String doPay(@PathVariable String orderUuid) {
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(1);
        order.setOrderPaymentTime(new Date());
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=-1";
    }

    @RequestMapping(path = "/order/return/{orderUuid}")
    public String returnGoods(@PathVariable String orderUuid) {
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(6);
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=-1";
    }

    @RequestMapping(path = "/order/cancel/{orderUuid}")
    public String cancelGoods(@PathVariable String orderUuid) {
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(5);
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=-1";
    }
}
