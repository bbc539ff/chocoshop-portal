package com.chocoshop.controller;

import com.chocoshop.model.Goods;
import com.chocoshop.model.Member;
import com.chocoshop.model.Order;
import com.chocoshop.service.GoodsService;
import com.chocoshop.service.OrderService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping(path = "/order/submit", method = RequestMethod.POST)
    public String generateOrder(@CookieValue String shoppingCart, @CookieValue String memberUuid, Order order, HttpServletResponse response) {
        Cookie cookie = new Cookie("shoppingCart", "");
        cookie.setPath("/");
        response.addCookie(cookie);

        order.setMemberUuid(memberUuid);
        order.setOrderStatus(0);
        order.setOrderGoodsList(shoppingCart);
        orderService.addOrder(order);
        return "redirect:/order/info?orderStatus=0";
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
        return "/order/order_info";
    }


    @RequestMapping(path = "/order/detail/{orderUuid}")
    public String orderDetail(Model model, @PathVariable String orderUuid) {
        try {
            Order order = orderService.findByUuid(orderUuid);

            String[] goodsList = order.getOrderGoodsList().split(",");
            Map<Goods, Integer> cartMap = new LinkedHashMap<>();
            for (String goods : goodsList) {
                if ("".equals(goods.trim())) continue;
                String id = goods.trim().split("/")[0];
                Goods key = goodsService.getGoodsById(Long.parseLong(id));
                System.out.println(key);
                String value = goods.trim().split("/")[1];
                cartMap.put(key, Integer.parseInt(value));
            }
            System.out.println("detail: " + cartMap);
            System.out.println("detail: " + order);



            model.addAttribute("cartMap", cartMap);
            model.addAttribute("order", order);
            return "/order/order_detail";
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
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=-1";
    }

    @RequestMapping(path = "/order/pay/{orderUuid}")
    public String doPay(@PathVariable String orderUuid) {
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(1);
        orderService.updateOrder(order);
        return "redirect:/order/info?orderStatus=2";
    }
}
