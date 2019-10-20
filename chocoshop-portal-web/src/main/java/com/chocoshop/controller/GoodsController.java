package com.chocoshop.controller;

import com.chocoshop.model.Category;
import com.chocoshop.model.Goods;
import com.chocoshop.service.CategoryService;
import com.chocoshop.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Controller
public class GoodsController {


    @Autowired
    GoodsService goodsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private MemcachedClient memCachedClient;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Deprecated
    @RequestMapping("/cart/info/depr")
    public String shoppingCart(@CookieValue(defaultValue = "") String shoppingCart, Model model) {
        try {
            String[] str = shoppingCart.split(",");
            LinkedHashMap<Goods, Integer> goodsMap = new LinkedHashMap<>();
            for (String goodsMsg : str) {
                if ("".equals(goodsMsg.trim()) ||
                        "undefined".equals(goodsMsg.trim().split("/")[0])) continue;

                Long goodsId = Long.parseLong(goodsMsg.trim().split("/")[0]);
                Integer num = Integer.parseInt(goodsMsg.trim().split("/")[1]);
                Goods goods = goodsService.getGoodsById(goodsId);
                goodsMap.put(goods, num);

            }
            model.addAttribute("goodsMap", goodsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "cart";
    }

    @RequestMapping(path = "/goods/search")
    @ResponseBody
    public List<Goods> search(Goods goods) throws InterruptedException, MemcachedException, TimeoutException {
        List<Goods> goodsList = memCachedClient.get("seach_goodsList");
        if (goodsList == null) {
            goodsList = goodsService.search(goods);
            memCachedClient.set("seach_goodsList", 30, goodsList);
        }
        logger.info("/goods/search: "+ goodsList);
        return goodsList;
    }

    @RequestMapping(path = "/goods/result")
    public String showGoods(Goods goods, Model model, HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "8") Integer pageSize) throws InterruptedException, MemcachedException, TimeoutException, IOException {
        // 限制搜索次数
        long m = 1000;
        int c = 4;
        if (!limitRequest(request, m, c)) {
            String msg = "访问次数过多";
            model.addAttribute("msg", msg);
            return "404";
        }

        try {
            // 读取 cookie 的 selected
            String selected = null;
            for (Cookie cookie : request.getCookies()) {
                if ("selected".equals(cookie.getName())) {
                    selected = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    System.out.println(selected);
                }
            }

            // 搜索商品结果
            PageHelper.startPage(pageNum, pageSize);
            List<Goods> result = new ArrayList<>();
            if (selected == null || "".equals(selected.trim()) || "[]".equals(selected.trim())) {
                List<Goods> goodsList = searchByGoods(goods, pageNum, pageSize);
                result.addAll(goodsList);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                List<Map> list = mapper.readValue(selected, List.class);
                // 遍历 categoryId
                for (Map<String, String> map : list) {
                    System.out.println(Long.parseLong(map.get("id")));
                    goods.setCategoryId(Long.parseLong(map.get("id")));
                    List<Goods> goodsList = searchByGoods(goods, pageNum, pageSize);

                    result.addAll(goodsList);
                }
            }

            model.addAttribute("pageNum", pageNum);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("goodsList", result);
            List<Category> categoryList = categoryService.getAllCategory();
            model.addAttribute("categoryList", categoryList);
            return "goods/search_result";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 限制访问一段时间访问次数
     *
     * @param request
     * @param m       毫秒数
     * @param c       次数
     * @return
     */
    private boolean limitRequest(HttpServletRequest request, long m, int c) {
        HttpSession session = request.getSession();
        Long mills = new Date().getTime();

        Object oldMills = session.getAttribute("time");
        if (oldMills != null && mills - (Long) oldMills <= m) {
            int count = (int) session.getAttribute("count") + 1;
            session.setAttribute("count", count);
            if (count >= c) {
                session.setAttribute("time", mills);
                return false;
            }
        } else {
            session.setAttribute("time", mills);
            session.setAttribute("count", 0);
        }
        return true;
    }

    public List<Goods> searchByGoods(Goods goods, Integer pageNum, Integer pageSize) throws InterruptedException, MemcachedException, TimeoutException {
        List<Goods> goodsList = memCachedClient.get(goods.toString()+pageNum+"&"+pageSize);
        if (goodsList == null) {
            goodsList = goodsService.search(goods);
            for (Goods goods1 : goodsList) {
                if (goods1.getGoodsImageurl() == null) continue;
                goods1.setGoodsImageurl(goods1.getGoodsImageurl().split(",")[0]);
            }
            memCachedClient.set(goods.toString(), 12, goodsList);
        } else {
        }
        return goodsList;
    }

    @RequestMapping(path = "/goods/detail/{goodsId}")
    public String showGoodsDetail(Model model, @PathVariable Long goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);

        String categoryName = categoryService.getByCategoryId(goods.getCategoryId()).getCategoryName();
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("goods", goods);
        return "goods/goods_detail";
    }


}
