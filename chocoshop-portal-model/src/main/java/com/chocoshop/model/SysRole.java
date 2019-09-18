package com.chocoshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="cc_sys_role")
public class SysRole implements Serializable {
    @Id
    private Integer roleId; // 编号
    private String roleName; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private Boolean roleAvailable; // 是否可用,如果不可用将不会添加给用户
    private String roleDescription; // 角色描述,UI界面显示使用


    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="cc_sys_role_perms",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="perm_id")})
    private List<SysPermission> permissions;

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="cc_sys_admin_role",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="admin_id")})
    private List<Admin> admins;// 一个角色对应多个用户


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getRoleAvailable() {
        return roleAvailable;
    }

    public void setRoleAvailable(Boolean roleAvailable) {
        this.roleAvailable = roleAvailable;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleAvailable=" + roleAvailable +
                ", roleDescription='" + roleDescription + '\'' +
                ", permissions=" + permissions +
                ", userInfos=" + admins +
                '}';
    }
}