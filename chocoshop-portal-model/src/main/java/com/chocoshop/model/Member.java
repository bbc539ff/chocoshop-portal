package com.chocoshop.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cc_member")
public class Member implements Serializable {

    @Id
    private String memberUuid;
    @Length(min = 6, max = 16, message = "用户名长度应为6-16")
    private String memberUserName;
    private String memberSalt;
    @Length(min = 6, max = 16, message = "密码长度应为6-16")
    private String memberPassword;
    @Length(min = 11, max = 11, message = "手机号码不合法")
    private String memberPhone;
    @Email
    private String memberEmail;
    private Date memberCreateTime;
    private Date memberUpdateTime;
    private Boolean memberGender;
    private String memberAddress;
    private Integer memberState;
    private String memberImageurl;
    private Double memberBalance;

    public String getMemberUuid() {
        return memberUuid;
    }

    public void setMemberUuid(String memberUuid) {
        this.memberUuid = memberUuid;
    }

    public String getMemberUserName() {
        return memberUserName;
    }

    public void setMemberUserName(String memberUserName) {
        this.memberUserName = memberUserName;
    }

    public String getMemberSalt() {
        return memberSalt;
    }

    public void setMemberSalt(String memberSalt) {
        this.memberSalt = memberSalt;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Date getMemberCreateTime() {
        return memberCreateTime;
    }

    public void setMemberCreateTime(Date memberCreateTime) {
        this.memberCreateTime = memberCreateTime;
    }

    public Date getMemberUpdateTime() {
        return memberUpdateTime;
    }

    public void setMemberUpdateTime(Date memberUpdateTime) {
        this.memberUpdateTime = memberUpdateTime;
    }

    public Boolean getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(Boolean memberGender) {
        this.memberGender = memberGender;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public Integer getMemberState() {
        return memberState;
    }

    public void setMemberState(Integer memberState) {
        this.memberState = memberState;
    }

    public String getMemberImageurl() {
        return memberImageurl;
    }

    public void setMemberImageurl(String memberImageurl) {
        this.memberImageurl = memberImageurl;
    }

    public Double getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(Double memberBalance) {
        this.memberBalance = memberBalance;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberUuid='" + memberUuid + '\'' +
                ", memberUserName='" + memberUserName + '\'' +
                ", memberSalt='" + memberSalt + '\'' +
                ", memberPassword='" + memberPassword + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", memberCreateTime=" + memberCreateTime +
                ", memberUpdateTime=" + memberUpdateTime +
                ", memberGender=" + memberGender +
                ", memberAddress='" + memberAddress + '\'' +
                ", memberState=" + memberState +
                ", memberImageurl='" + memberImageurl + '\'' +
                ", memberBalance=" + memberBalance +
                '}';
    }
}
