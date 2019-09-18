package com.chocoshop.service;

import com.chocoshop.mapper.MemberMapper;
import com.chocoshop.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.Utils;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    MemberMapper memberMapper;

    public List<Member> showAllMembers(){
        return memberMapper.selectAll();
    }

    public Member findByMemberName(String memberUserName){
        Member member = new Member();
        member.setMemberUserName(memberUserName);
        return memberMapper.selectOne(member);
    }

    public int addMember(Member member, MultipartFile file){
        member.setMemberUuid(UUID.randomUUID().toString());

        if(member.getMemberPassword() != null) {
            String pwd = member.getMemberPassword();
            String salt = Utils.generateSalt(pwd);
            String newPwd = Utils.generatePwd(pwd, salt);
            member.setMemberPassword(newPwd);
            member.setMemberSalt(salt);
        }

        // set imgurl

        String path = Utils.uploadSingle(file, "/upload/member/member_image/", member.getMemberUuid(), false);
        member.setMemberImageurl(path);


        if(member.getMemberState() == null) member.setMemberState(0);
        if(member.getMemberCreateTime() == null) member.setMemberCreateTime(new Date());
        if(member.getMemberUpdateTime() == null) member.setMemberUpdateTime(new Date());
        return memberMapper.insert(member);
    }

    public int deleteMember(Member member){
        return memberMapper.delete(member);
    }

    public int updateMember(Member member, MultipartFile file){
        if(member.getMemberPassword() != null) {
            String pwd = member.getMemberPassword();
            String salt = Utils.generateSalt(pwd);
            String newPwd = Utils.generatePwd(pwd, salt);
            member.setMemberPassword(newPwd);
            member.setMemberSalt(salt);
        }

        if(file != null){
            // set imgurl
            String path = Utils.uploadSingle(file, "/upload/member/member_image", member.getMemberUuid(), false);
            member.setMemberImageurl(path);
        }

        member.setMemberUpdateTime(new Date());
        System.out.println(member);
        int returnVal = 0;
        try{
            returnVal = memberMapper.updateByPrimaryKeySelective(member);
            System.out.println(returnVal);
        } catch (Exception e){
            e.printStackTrace();
        }
        return returnVal;
    }

    public List<Member> search(Member member){
        try {
            return memberMapper.search(member);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int countMember(){
        return memberMapper.selectCount(new Member());
    }
}
