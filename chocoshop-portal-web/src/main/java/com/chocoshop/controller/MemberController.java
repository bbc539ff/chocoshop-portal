package com.chocoshop.controller;

import com.chocoshop.model.Member;
import com.chocoshop.service.MemberService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @RequestMapping("/member/info")
    public String userInfo(Model model){
        Member member = null;
        if(SecurityUtils.getSubject().isAuthenticated()){
            member = (Member) SecurityUtils.getSubject().getPrincipal();
            member = memberService.findByMemberName(member.getMemberUserName());
            model.addAttribute("member", member);
            return "member/member_info";
        }
        return "login";
    }

    @RequestMapping(path = "/member/edit", method = RequestMethod.POST)
    public String edit(Member member, HttpServletRequest request, HttpServletResponse response) {
        try{
            if(member.getMemberUserName() != null) { // 修改了用户名，需要注销
                for (Cookie cookie : request.getCookies()) {
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                SecurityUtils.getSubject().logout();
            }
            memberService.updateMember(member, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/member/info";
    }

    @ExceptionHandler(Exception.class)
    public void myExceptionHandler(Exception e){
        e.printStackTrace();
    }

}
