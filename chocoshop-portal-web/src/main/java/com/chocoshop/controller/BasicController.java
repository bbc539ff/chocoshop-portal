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
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class BasicController {

    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GoodsService goodsService;

    Logger logger = LoggerFactory.getLogger(BasicController.class);

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, String username, String password, Model model) {
        try{
            // 校验验证码
            String sessVerifyCode  = (String) request.getSession().getAttribute("verifyCode");
            String reqVerifyCode = request.getParameter("verifyCode");
            logger.info("session - captcha", sessVerifyCode+" - "+reqVerifyCode);
            if(!Objects.equals(sessVerifyCode, reqVerifyCode)) {
                model.addAttribute("msg", "验证码错误");
                return "login";
            }

            // 如果没有登录，则登录， 否则跳转index
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                AuthenticationToken token = new UsernamePasswordToken(username, password);
                SecurityUtils.getSubject().login(token);

                // 设置Cookies,Session
                Member member = memberService.findByMemberName(username);
                Cookie cookie = null;
                cookie = new Cookie("memberUuid", member.getMemberUuid());
                cookie.setPath("/");
                response.addCookie(cookie);
                cookie = new Cookie("memberName", member.getMemberUserName());
                cookie.setPath("/");
                response.addCookie(cookie);
                HttpSession session = request.getSession();
                session.setAttribute("memberUuid", member.getMemberUuid());
                session.setAttribute("memberName", member.getMemberUserName());
            } else {
                return "redirect:/index";
            }

        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户不存在");
            return "login";
        } catch (IncorrectCredentialsException e){
            model.addAttribute("msg", "密码错误");
            return "login";
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg", "未知错误");
            return "redirect:/login";
        }

        return "redirect:/index";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@Validated Member member, @RequestParam("file") MultipartFile file, Model model) {
        logger.info("register: "+member.toString());

        // add member
        if (memberService.addMember(member, file) == 1) {
            return "login";
        } else {
            String msg = "unknown error";
            model.addAttribute("msg", msg);
            return "register";
        }
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, Model model) {
        for (FieldError fieldError : e.getFieldErrors()) {
            logger.info(fieldError.getField()+"Msg");
            model.addAttribute(fieldError.getField()+"Msg", fieldError.getDefaultMessage());
        }

        return "register";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除Cookies
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
        logger.info("Hello");
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
        return "index";
    }

    @RequestMapping("/navbar")
    public String navbar() {
        return "navbar";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "footer";
    }

}
