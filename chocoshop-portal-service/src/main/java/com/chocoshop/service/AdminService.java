package com.chocoshop.service;

import com.chocoshop.mapper.AdminMapper;
import com.chocoshop.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.util.Date;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public Admin findByAdminName(String adminName){
        return adminMapper.findByAdminName(adminName);
    }

    public List<Admin> findAll(){
        return adminMapper.selectAll();
    }

    public int addAdmin(Admin admin){

        if(admin.getAdminPassword() != null) {
            String pwd = admin.getAdminPassword();
            String salt = Utils.generateSalt(pwd);
            String newPwd = Utils.generatePwd(pwd, salt);
            admin.setAdminPassword(newPwd);
            admin.setAdminSalt(salt);
        }
        if(admin.getAdminState() != null) admin.setAdminState(false);
        if(admin.getAdminCreateTime() != null) admin.setAdminCreateTime(new Date());
        if(admin.getAdminUpdateTime() != null) admin.setAdminUpdateTime(new Date());
        System.out.println(admin);
        return adminMapper.insert(admin);
    }

    public int deleteAdmin(Admin admin){
        return adminMapper.delete(admin);
    }

    public int updateAdmin(Admin admin){
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    public int countAdmin(){
        return adminMapper.selectCount(new Admin());
    }

    public List<Admin> search(Admin admin){
        try {
            return adminMapper.search(admin);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
