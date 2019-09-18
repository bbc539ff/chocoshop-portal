package com.chocoshop.controller;

import com.chocoshop.config.RequestLimit;
import com.chocoshop.model.Category;
import com.chocoshop.model.Goods;
import com.chocoshop.model.Order;
import com.chocoshop.service.CategoryService;
import com.chocoshop.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Controller
public class UserGoodsController {


    @Autowired
    GoodsService goodsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private MemcachedClient memCachedClient;

    @RequestMapping("/user/goods/shopping-cart")
    public String shoppingCart(@CookieValue(defaultValue = "") String shoppingCart, Model model) {
        try {
            String[] str = shoppingCart.split(",");
            LinkedHashMap<Goods, Integer> goodsMap = new LinkedHashMap<>();
            for (String goodsMsg : str) {
                if ("".equals(goodsMsg.trim())) continue;

                Long goodsId = Long.parseLong(goodsMsg.trim().split("/")[0]);
                Integer num = Integer.parseInt(goodsMsg.trim().split("/")[1]);
                Goods goods = goodsService.getGoodsById(goodsId);
                goodsMap.put(goods, num);

            }
            model.addAttribute("goodsMap", goodsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/user/shopping_cart";
    }

    @RequestMapping(path = "/user/goods/search")
    @ResponseBody
    public List<Goods> search(Goods goods) throws InterruptedException, MemcachedException, TimeoutException {
        System.out.println(goods);


        List<Goods> goodsList = memCachedClient.get("seach_goodsList");
        if(goodsList == null){
            goodsList = goodsService.search(goods);
            memCachedClient.set("seach_goodsList", 30, goodsList);
        }


        System.out.println(goodsList);
        return goodsList;
    }

    @RequestMapping(path = "/user/goods/show-goods")
    public String showGoods(Goods goods, Model model, HttpServletRequest request) throws InterruptedException, MemcachedException, TimeoutException, IOException {
        // 限制搜索次数
        long m = 1000;
        int c = 4;

        HttpSession session = request.getSession();
        Long mills = new Date().getTime();

        Object oldMills = session.getAttribute("time");
        if(oldMills != null && mills - (Long)oldMills <= m) {
            int count = (int)session.getAttribute("count")+1;
            session.setAttribute("count", count);
            if(count >= c){
                session.setAttribute("time", mills);
                return  "400";
            }
        } else {
            session.setAttribute("time", mills);
            session.setAttribute("count", 0);
        }

        try{
            String selected = null;
            for(Cookie cookie : request.getCookies()){
                if("selected".equals(cookie.getName())){
                    selected = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    System.out.println(selected);
                }
            }

            List<Goods> result = new ArrayList<>();

            if(selected == null || "".equals(selected.trim()) || "[]".equals(selected.trim())){
                List<Goods> goodsList = searchByGoods(goods);
                result.addAll(goodsList);
            } else{
                ObjectMapper mapper = new ObjectMapper();
                List<Map> list = mapper.readValue(selected, List.class);
                // 遍历 categoryId
                for(Map<String, String> map : list){
                    System.out.println(Long.parseLong(map.get("id")));
                    goods.setCategoryId(Long.parseLong(map.get("id")));
                    List<Goods> goodsList = searchByGoods(goods);

                    result.addAll(goodsList);
                }
            }

            model.addAttribute("goodsList", result);
            List<Category> categoryList = categoryService.getAllCategory();
            model.addAttribute("categoryList", categoryList);
            return "/user/show_goods";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public List<Goods> searchByGoods(Goods goods) throws InterruptedException, MemcachedException, TimeoutException {
        List<Goods> goodsList = memCachedClient.get(goods.toString());
        if(goodsList == null){
            goodsList = goodsService.search(goods);
            for (Goods goods1 : goodsList) {
                if(goods1.getGoodsImageurl() == null) continue;
                goods1.setGoodsImageurl(goods1.getGoodsImageurl().split(",")[0]);
            }
            System.out.println("no cached");
            memCachedClient.set(goods.toString(), 12, goodsList);
        } else {
            System.out.println("cached");
        }
        return goodsList;
    }

    @RequestMapping(path = "/user/goods/{goodsId}")
    public String showGoodsDetail(Model model, @PathVariable Long goodsId){
        Goods goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods", goods);
        return "/user/goods_detail";
    }

    @RequestMapping(path = "/user/goods/new-goods")
    public String getNewGoods(Model model){
        List<Category> categoryList = categoryService.getAllCategory();
        List<NewGoods> newGoodsList = new ArrayList<>();
        for(Category category : categoryList){
            if(category.getCategoryParent() == null) {
                NewGoods newGoods = new NewGoods();
                newGoods.setCategoryName(category.getCategoryName());

                PageHelper.startPage(1, 16);
                List<Goods> goodsList = goodsService.newGoods(category.getCategoryId());
                for (Goods goods : goodsList) {
                    goods.setGoodsImageurl(goods.getGoodsImageurl().split(",")[0]);
                }
                System.out.println(category.getCategoryId()+":"+goodsList);
                newGoods.setGoodsList(goodsList);
                newGoodsList.add(newGoods);
            }
        }

        model.addAttribute("newGoodsList", newGoodsList);
        return "/user/index";
    }

    class NewGoods implements Serializable {
        String categoryName;
        List<Goods> goodsList;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<Goods> goodsList) {
            this.goodsList = goodsList;
        }

    }

}
