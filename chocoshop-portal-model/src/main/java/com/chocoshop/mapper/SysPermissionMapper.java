package com.chocoshop.mapper;

import com.chocoshop.model.SysPermission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends tk.mybatis.mapper.common.Mapper<SysPermission>{

    @Select("SELECT cc_sys_perms.* FROM cc_sys_role_perms, cc_sys_perms WHERE cc_sys_role_perms.perm_id = cc_sys_perms.perm_id AND cc_sys_role_perms.role_id = #{roleId}")
    @Results({
            @Result(property = "permId", column = "perm_id"),
            @Result(property = "permAvailable", column = "perm_available"),
            @Result(property = "permName", column = "perm_name"),
            @Result(property = "permParentId", column = "perm_parent_id"),
            @Result(property = "permParentIds", column = "perm_parent_ids"),
            @Result(property = "permString", column = "perm_string"),
            @Result(property = "permResourceType", column = "perm_resource_type"),
            @Result(property = "permUrl", column = "perm_url"),

//            @Result(property = "roles", column = "perm_id", many = @Many(select = "com.chocoshop.mapper.SysRoleMapper.selectByPermId")),

    })
    public List<SysPermission> selectByRoleId(Integer roleId);


}
