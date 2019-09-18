package com.chocoshop.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="cc_sys_perms")
public class SysPermission implements Serializable {
    @Id
    private Integer permId;
    private Boolean permAvailable = Boolean.FALSE;;
    private String permName;
    private Long permParentId; //父编号
    private String permParentIds; //父编号列表
    private String permString; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String permResourceType;
    private String permUrl;//资源路径.


    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<SysRole> roles;


    public Integer getPermId() {
        return permId;
    }

    public void setPermId(Integer permId) {
        this.permId = permId;
    }

    public Boolean getPermAvailable() {
        return permAvailable;
    }

    public void setPermAvailable(Boolean permAvailable) {
        this.permAvailable = permAvailable;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public Long getPermParentId() {
        return permParentId;
    }

    public void setPermParentId(Long permParentId) {
        this.permParentId = permParentId;
    }

    public String getPermParentIds() {
        return permParentIds;
    }

    public void setPermParentIds(String permParentIds) {
        this.permParentIds = permParentIds;
    }

    public String getPermString() {
        return permString;
    }

    public void setPermString(String permString) {
        this.permString = permString;
    }

    public String getPermResourceType() {
        return permResourceType;
    }

    public void setPermResourceType(String permResourceType) {
        this.permResourceType = permResourceType;
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
                "permId=" + permId +
                ", permAvailable=" + permAvailable +
                ", permName='" + permName + '\'' +
                ", permParentId=" + permParentId +
                ", permParentIds='" + permParentIds + '\'' +
                ", permString='" + permString + '\'' +
                ", permResourceType='" + permResourceType + '\'' +
                ", permUrl='" + permUrl + '\'' +
                ", roles=" + roles +
                '}';
    }
}