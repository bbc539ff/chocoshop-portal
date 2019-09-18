package com.chocoshop.controller;

import com.chocoshop.model.Member;
import com.chocoshop.model.Order;
import com.chocoshop.service.OrderService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserOrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(path = "/user/order/submit", method = RequestMethod.POST)
    public String generateOrder(@CookieValue String shoppingCart, @CookieValue String memberUuid, Order order, HttpServletResponse response){
        Cookie cookie = new Cookie("shoppingCart","");
        cookie.setPath("/");
        response.addCookie(cookie);

        order.setMemberUuid(memberUuid);
        order.setOrderStatus(0);
        order.setOrderGoodsList(shoppingCart);
        orderService.addOrder(order);
        return "redirect:/user/order/show-order";
    }

    @RequestMapping(path = "/user/order/show-order")
    public String userOrder(Model model){
        Member member = (Member) SecurityUtils.getSubject().getPrincipal();
        List<Order> orderList = orderService.findByMemberUuid(member.getMemberUuid());
        model.addAttribute("orderList", orderList);
        return "/user/show_order";
    }

    @RequestMapping(path = "/user/order/show-order/{orderUuid}")
    public String orderDetail(Model model, @PathVariable String orderUuid) {
        try {
            Order order = orderService.findByUuid(orderUuid);

            String[] goodsList = order.getOrderGoodsList().split(",");
            Map<String, Integer> cartMap = new LinkedHashMap<>();
            for(String goods : goodsList){
                if("".equals(goods.trim())) continue;
                String key = goods.trim().split("/")[0];
                String value = goods.trim().split("/")[1];
                cartMap.put(key, Integer.parseInt(value));
            }
            System.out.println("detail: "+cartMap);
            System.out.println("detail: "+order);

            model.addAttribute("cartMap", cartMap);
            model.addAttribute("order", order);
            return "/user/order_detail";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "403";
    }

    @RequestMapping(path = "/user/order/consign/{orderUuid}")
    public String consign(@PathVariable String orderUuid){
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(3);
        orderService.updateOrder(order);
        return "redirect:/user/order/show-order";
    }

    @RequestMapping(path = "/user/order/pay/{orderUuid}")
    public String doPay(@PathVariable String orderUuid){
        Order order = new Order();
        order.setOrderUuid(orderUuid);
        order.setOrderStatus(1);
        orderService.updateOrder(order);
        return "redirect:/user/order/show-order";
    }
}
