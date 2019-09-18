package com.chocoshop.mapper;

import com.chocoshop.model.Admin;
import com.chocoshop.model.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SysRoleMapper extends tk.mybatis.mapper.common.Mapper<SysRole> {

    @Select("SELECT cc_sys_role.* FROM cc_sys_role, cc_sys_admin_role WHERE admin_id = #{adminId} AND cc_sys_role.role_id = cc_sys_admin_role.role_id")
    @Results({
            @Result(property ="roleId", column = "role_id"),
            @Result(property ="roleName", column = "role_name"),
            @Result(property ="roleAvailable", column = "role_available"),
            @Result(property ="roleDescription", column = "role_description"),
            @Result(property = "permissions", column = "role_id", many = @Many(select = "com.chocoshop.mapper.SysPermissionMapper.selectByRoleId")),
    })
    public List<SysRole> selectByAdminId(Long adminId);


    @Select("SELECT cc_sys_role.* FROM cc_sys_role, cc_sys_role_perms WHERE perm_id = #{permId} AND cc_sys_role.role_id = cc_sys_role_perms.role_id")
    @Results({
            @Result(property ="roleId", column = "role_id"),
            @Result(property ="roleName", column = "role_name"),
            @Result(property ="roleAvailable", column = "role_available"),
            @Result(property ="roleDescription", column = "role_description"),
            @Result(property = "permissions", column = "role_id", many = @Many(select = "com.chocoshop.mapper.SysPermissionMapper.selectByRoleId")),
    })
    public List<SysRole> selectByPermId(Integer permId);

    @Select("SELECT cc_sys_role.* FROM cc_sys_role")
    @Results({
            @Result(property ="roleId", column = "role_id"),
            @Result(property ="roleName", column = "role_name"),
            @Result(property ="roleAvailable", column = "role_available"),
            @Result(property ="roleDescription", column = "role_description"),
            @Result(property = "permissions", column = "role_id", many = @Many(select = "com.chocoshop.mapper.SysPermissionMapper.selectByRoleId")),
    })
    @Override
    public List<SysRole> selectAll();

    @Select("<script>" +
            "SELECT * FROM cc_sys_role " +
            "<where>" +
            "<if test=\"roleId != null\">" +
            "role_id = #{roleId}" +
            "</if>" +
            "<if test=\"roleName != null\">" +
            "AND role_name = #{roleName}" +
            "</if>" +
            "<if test=\"roleAvailable != null\">" +
            "AND role_available = #{roleAvailable}" +
            "</if>" +
            "<if test=\"roleDescription != null\">" +
            "AND role_description = #{roleDescription}" +
            "</if>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "roleAvailable", column = "role_available"),
            @Result(property = "roleDescription", column = "role_description"),
            @Result(property = "permissions", column = "role_id", many = @Many(select = "com.chocoshop.mapper.SysPermissionMapper.selectByRoleId")),
    })
    List<SysRole> search(SysRole sysRole);

    @Insert("INSERT INTO cc_sys_role_perms VALUE(#{roleId}, #{permId})")
    int insertPerm(@Param("roleId") Integer roleId, @Param("permId") Integer permId);

    @Delete("DELETE FROM cc_sys_role_perms WHERE role_id = #{roleId}")
    int deletePerm(@Param("roleId") Integer roleId);

    @Insert("INSERT INTO cc_sys_admin_role VALUE (#{roleId}, #{adminId})")
    int addRoleToAdmin(Integer roleId, Integer adminId);

    @Insert("DELETE FROM cc_sys_admin_role WHERE admin_id = #{adminId}")
    int deleteRoleFromAdmin(Integer adminId);
}
