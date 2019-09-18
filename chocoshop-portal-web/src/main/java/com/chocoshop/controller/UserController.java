package com.chocoshop.controller;

import com.chocoshop.model.Category;
import com.chocoshop.model.Goods;
import com.chocoshop.model.Member;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    MemberService memberService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String login() {
        return "/user/login";
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, String username, String password){

        if(!SecurityUtils.getSubject().isAuthenticated()){
            AuthenticationToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            return "/user/index";
        }
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = "";
        if (exception != null) {
            System.out.println("exception=" + exception);
            System.out.println("error...");
        }
        return "/user/login";
    }


    @RequestMapping(path = "/user/register", method = RequestMethod.GET)
    public String register(){
        return "/user/register";
    }

    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public String register(@Validated Member member, @RequestParam("file") MultipartFile file, BindingResult bindingResult, Model model){
        System.out.println(member);
        if(bindingResult.hasErrors()) {
            String msg = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                msg += fieldError + ", ";
            }
            model.addAttribute("msg", msg);
            return "/user/register";
        }

        try{
            if(memberService.addMember(member, file) == 1) {
                return "/user/login";
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(path = "/user/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "/user/index";
    }


    @RequestMapping({"/user/index", "/"})
    public String index(Model model){
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

    @RequestMapping("/user/navbar")
    public String navbar(@CookieValue(defaultValue = "") String memberUuid, @CookieValue(defaultValue = "") String memberName, Model model, HttpServletResponse response){
        if(("".equals(memberUuid) || "".equals(memberName)) &&
                SecurityUtils.getSubject().isAuthenticated()){
            Member member = (Member) SecurityUtils.getSubject().getPrincipal();
            memberUuid = member.getMemberUuid();
            memberName = member.getMemberUserName();
            Cookie memberUuidCookie = new Cookie("memberUuid", memberUuid);
            Cookie memberNameCookie = new Cookie("memberName", memberName);
            response.addCookie(memberUuidCookie);
            response.addCookie(memberNameCookie);
        }

        model.addAttribute("memberUuid", memberUuid);
        model.addAttribute("memberName", memberName);
        return "/user/navbar";
    }

    @RequestMapping("/user/user-info")
    public String userInfo(Model model){
        Member member = null;
        if(SecurityUtils.getSubject().isAuthenticated()){
            member = (Member) SecurityUtils.getSubject().getPrincipal();
            member = memberService.findByMemberName(member.getMemberUserName());
            model.addAttribute("member", member);
            return "/user/user_info";
        }
        return null;
    }

    @RequestMapping(path = "/user/user-info/edit", method = RequestMethod.POST)
    public String edit(Member member){
        try{
            memberService.updateMember(member, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/user-info";
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
