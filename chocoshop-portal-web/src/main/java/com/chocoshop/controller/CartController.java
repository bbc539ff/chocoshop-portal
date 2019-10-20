package com.chocoshop.controller;

import com.chocoshop.model.Goods;
import com.chocoshop.model.json.Cart;
import com.chocoshop.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(CartController.class);

    /**
     * 向购物车添加商品
     * @param goodsId 添加商品的id
     * @param number 添加商品数量
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return success/error
     * @throws IOException
     */
    @GetMapping("/cart/add")
    @ResponseBody
    public String addCart(Long goodsId, int number, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 从数据库获取要添加的商品
        Goods goods = null;
        if((goods = goodsService.getGoodsById(goodsId)) == null) return "check your goodsId!";

        try{
            Cart cart = cartDecode(request.getCookies());

            // 更新Cart对象的值
            Map<Long, Integer> map = cart.getGoodsIdMap();
            if(map.containsKey(goodsId)) { // 购物车里已有该商品
                map.put(goodsId, map.get(goodsId)+number);
            } else {
                map.put(goodsId, number);
                cart.setNumber(cart.getNumber()+1);
            }
            cart.setGoodsIdMap(map);
            cart.setPrice(cart.getPrice().add(goods.getGoodsPrice().multiply(new BigDecimal(number))));

            // Cart对象转JSON对象并URLEncode
            String cartJSON = objectMapper.writeValueAsString(cart);
            String cookieContent = URLEncoder.encode(cartJSON, "utf-8");

            // 更新后的Cart的JSON写入Cookie
            Cookie cookie = new Cookie("cart", cookieContent);
            logger.info("path: "+cookie.getPath());
            cookie.setPath("/");
            response.addCookie(cookie);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 删除购物车的商品
     * @param goodsId 删除的商品id
     * @param number 删除数量
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return success/error
     * @throws IOException
     */
    @GetMapping("/cart/del")
    @ResponseBody
    public String delCart(Long goodsId, int number, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 从数据库获取要添加的商品
        Goods goods = null;
        if((goods = goodsService.getGoodsById(goodsId)) == null) return "check your goodsId!";

        Cart cart = cartDecode(request.getCookies());

        // 删除商品
        Map<Long, Integer> goodsIdMap = cart.getGoodsIdMap();
        int newNumber = goodsIdMap.get(goodsId)-number;
        if(newNumber <= 0){ // 移除整个商品
            cart.setPrice(cart.getPrice().subtract(goods.getGoodsPrice().multiply(new BigDecimal(goodsIdMap.get(goodsId)))));
            goodsIdMap.remove(goodsId);
            cart.setNumber(cart.getNumber()-1);
        } else{ // 减少数量
            cart.setPrice(cart.getPrice().subtract(goods.getGoodsPrice().multiply(new BigDecimal(number))));
            goodsIdMap.put(goodsId, newNumber);
        }
        cart.setGoodsIdMap(goodsIdMap);

        // Cart对象转JSON对象并URLEncode
        String cartJSON = objectMapper.writeValueAsString(cart);
        String cookieContent = URLEncoder.encode(cartJSON, "utf-8");
        // // 更新后的Cart的JSON写入Cookie
        Cookie cookie = new Cookie("cart", cookieContent);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "success";
    }

    @RequestMapping("/cart/info")
    public String cartInfo(@CookieValue(defaultValue = "") String cart, HttpServletRequest request, Model model) throws IOException {
        // String cart转换为Cart对象
        Cart cartObject = cartDecode(request.getCookies());

        // 取出goodsIdMap
        Map<Long, Integer> goodsIdMap = cartObject.getGoodsIdMap();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "cart";
    }

    /**
     * 工具类
     * 获取Cookie并转换成Cart
     * @param cookies cookie集合
     * @throws IOException
     */
    public Cart cartDecode(Cookie[] cookies) throws IOException {
        // 获取购物车的Cookie
        int index = -1;
        for(int i = 0;cookies!=null&&i<cookies.length;i++){
            if(Objects.equals(cookies[i].getName(), "cart")){
                index = i;
                logger.info("cookie: "+cookies[i].getName()+" "+cookies[i].getValue());
                break;
            }
        }

        // 根据Cookie创建Cart对象
        Cart cart = null;
        if(index != -1){ // Cookie存在
            Cookie cookie = cookies[index];
            Map map = (Map) objectMapper.readValue(URLDecoder.decode(cookie.getValue(), "utf-8"), Map.class);
            cart = objectMapper.convertValue(map, Cart.class);
        } else {
            cart = new Cart(new HashMap<>(), 0, new BigDecimal(0));
        }
        logger.info(cart.toString());
        return cart;
    }

    /**
     * 删除指定商品（用于生成订单）
     */
    public Cart multiDel(List<Long> goodsIdList, Cookie[] cookies) throws IOException {
        Cart cart = cartDecode(cookies);
        Map<Long, Integer> goodsIdMap = cart.getGoodsIdMap();
        for (Long goodsId : goodsIdList) {
            Goods goods = null;
            if((goods = goodsService.getGoodsById(goodsId)) == null) return null;
            logger.info("multiDel(): "+goods.toString());
            cart.setNumber(cart.getNumber()-1);
            cart.setPrice(cart.getPrice().subtract(goods.getGoodsPrice().multiply(new BigDecimal(goodsIdMap.get(goodsId)))));
            goodsIdMap.remove(goodsId);
        }
        cart.setGoodsIdMap(goodsIdMap);
        return cart;
    }

    @ExceptionHandler(Exception.class)
    public void myExceptionHandler(Exception e){
        e.printStackTrace();
    }
}
