package com.chocoshop.controller;

import com.chocoshop.model.Member;
import com.chocoshop.service.MemberService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
            return "/member/member_info";
        }
        return "/login";
    }

    @RequestMapping(path = "/member/edit", method = RequestMethod.POST)
    public String edit(Member member){
        try{
            memberService.updateMember(member, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/member/info";
    }

}
