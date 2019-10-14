package com.chocoshop.controller;

import com.chocoshop.model.Category;
import com.chocoshop.model.Goods;
import com.chocoshop.model.Member;
import com.chocoshop.model.json.NewGoods;
import com.chocoshop.service.CategoryService;
import com.chocoshop.service.GoodsService;
import com.chocoshop.service.MemberService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BasicController {

    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, String username, String password) {
        // login
        try{
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                AuthenticationToken token = new UsernamePasswordToken(username, password);
                SecurityUtils.getSubject().login(token);

                // handle exception
                String exception = (String) request.getAttribute("shiroLoginFailure");
                String msg = "";
                if (exception != null) {
                    System.out.println("exception=" + exception);
                    return "/login";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/index";

    }


    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register() {
        return "/register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@Validated Member member, @RequestParam("file") MultipartFile file, BindingResult bindingResult, Model model) {
        System.out.println(member);
        // validate exception
        if (bindingResult.hasErrors()) {
            String msg = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                msg += fieldError + ", ";
            }
            model.addAttribute("msg", msg);
            return "/register";
        }

        // add member
        if (memberService.addMember(member, file) == 1) {
            return "/login";
        } else {
            String msg = "unknown error";
            model.addAttribute("msg", msg);
            return "/register";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear cookies
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        SecurityUtils.getSubject().logout();
        return "redirect:/index";
    }


    @RequestMapping({"/index", "/"})
    public String index(Model model) {
        List<Category> categoryList = categoryService.getAllCategory();

        // List 的每一项是每个分类的新商品
        List<NewGoods> newGoodsList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getCategoryParent() == null) {
                NewGoods newGoods = new NewGoods();
                newGoods.setCategoryName(category.getCategoryName());

                PageHelper.startPage(1, 16);
                List<Goods> goodsList = goodsService.newGoods(category.getCategoryId());
                for (Goods goods : goodsList) {
                    goods.setGoodsImageurl(goods.getGoodsImageurl().split(",")[0]);
                }
                newGoods.setGoodsList(goodsList);
                newGoodsList.add(newGoods);
            }
        }
        List<Goods> hot = goodsService.findBySellNumber();

        if(hot != null){
            for(Goods goods : hot){
                goods.setGoodsImageurl(goods.getGoodsImageurl().split(",")[0]);
            }
        }

        model.addAttribute("hot", hot);
        model.addAttribute("newGoodsList", newGoodsList);
        return "/index";
    }

    @RequestMapping("/navbar")
    public String navbar(@CookieValue(defaultValue = "") String memberUuid, @CookieValue(defaultValue = "") String memberName, Model model, HttpServletResponse response) {
        if (("".equals(memberUuid) || "".equals(memberName)) &&
                SecurityUtils.getSubject().isAuthenticated()) {
            Member member = (Member) SecurityUtils.getSubject().getPrincipal();
            memberUuid = member.getMemberUuid();
            memberName = member.getMemberUserName();
            Cookie memberUuidCookie = new Cookie("memberUuid", memberUuid);
            Cookie memberNameCookie = new Cookie("memberName", memberName);
            response.addCookie(memberUuidCookie);
            response.addCookie(memberNameCookie);
            model.addAttribute("memberUuid", memberUuid);
            model.addAttribute("memberName", memberName);
        }

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            Cookie cookie = new Cookie("memberUuid", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            Cookie cookie2 = new Cookie("memberName", null);
            cookie2.setPath("/");
            cookie2.setMaxAge(0);
            response.addCookie(cookie2);
            model.addAttribute("memberUuid", null);
            model.addAttribute("memberName", null);
        }
        return "/navbar";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "footer";
    }
}
