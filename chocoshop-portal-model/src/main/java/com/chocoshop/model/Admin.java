package com.chocoshop.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="cc_admin")
public class Admin implements Serializable {

    @Id
    private Integer adminId;
    @Column(unique = true)
    @Length(min = 6, max = 18)
    private String adminName;//帐号
    @Length(min = 6, max = 18)
    private String adminNickname;//名称（昵称或者真实姓名，不同系统不同定义）
    @Length(min = 6, max = 18)
    private String adminPassword; //密码;
    private String adminSalt;//加密密码的盐
    private Boolean adminState;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    @Length(min = 11, max = 11)
    private String adminPhone;
    @Email
    private String adminEmail;
    private Date adminCreateTime;
    private Date adminUpdateTime;
    private Boolean adminGender;
    private String adminAddress;
    private String adminPhoto;


    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "cc_sys_admin_role", joinColumns = { @JoinColumn(name = "admin_id") }, inverseJoinColumns ={@JoinColumn(name = "role_id") })
    private List<SysRole> roleList;// 一个用户具有多个角色

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminNickname() {
        return adminNickname;
    }

    public void setAdminNickname(String adminNickname) {
        this.adminNickname = adminNickname;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminSalt() {
        return adminSalt;
    }

    public void setAdminSalt(String adminSalt) {
        this.adminSalt = adminSalt;
    }

    public Boolean getAdminState() {
        return adminState;
    }

    public void setAdminState(Boolean adminState) {
        this.adminState = adminState;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Date getAdminCreateTime() {
        return adminCreateTime;
    }

    public void setAdminCreateTime(Date adminCreateTime) {
        this.adminCreateTime = adminCreateTime;
    }

    public Date getAdminUpdateTime() {
        return adminUpdateTime;
    }

    public void setAdminUpdateTime(Date adminUpdateTime) {
        this.adminUpdateTime = adminUpdateTime;
    }

    public Boolean getAdminGender() {
        return adminGender;
    }

    public void setAdminGender(Boolean adminGender) {
        this.adminGender = adminGender;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public String getAdminPhoto() {
        return adminPhoto;
    }

    public void setAdminPhoto(String adminPhoto) {
        this.adminPhoto = adminPhoto;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", adminNickname='" + adminNickname + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", adminSalt='" + adminSalt + '\'' +
                ", adminState=" + adminState +
                ", adminPhone='" + adminPhone + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminCreateTime=" + adminCreateTime +
                ", adminUpdateTime=" + adminUpdateTime +
                ", adminGender=" + adminGender +
                ", adminAddress='" + adminAddress + '\'' +
                ", adminPhoto='" + adminPhoto + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}