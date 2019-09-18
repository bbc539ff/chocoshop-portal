package com.chocoshop.service;

import com.chocoshop.mapper.SysRoleMapper;
import com.chocoshop.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;

    public List<SysRole> showAllRole(){
        return sysRoleMapper.selectAll();
    }

    @Transactional
    public int addSysRole(SysRole sysRole, String permIds) {
        int returnVal = 0;
        try {
            returnVal = sysRoleMapper.insert(sysRole);
            int roleId = sysRoleMapper.selectOne(sysRole).getRoleId();
            System.out.println(permIds);
            String[] perms = permIds.split(",");
            for(String id : perms){
                returnVal = sysRoleMapper.insertPerm(roleId, Integer.parseInt(id.trim()));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return returnVal;
    }

    @Transactional
    public int deleteSysRole(SysRole sysRole){
        sysRoleMapper.deletePerm(sysRole.getRoleId());
        return sysRoleMapper.delete(sysRole);
    }

    @Transactional
    public int updateSysRole(SysRole sysRole, String permIds){
        int returnVal = 0;
        try {
            // 更新 Role
            returnVal = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
            int roleId = sysRole.getRoleId();

            // 先删除 roleId 下的所有权限，再插入新权限
            sysRoleMapper.deletePerm(sysRole.getRoleId());
            String[] perms = permIds.split(",");
            for(String id : perms){
                if("".equals(id.trim())) continue;
                returnVal = sysRoleMapper.insertPerm(roleId, Integer.parseInt(id.trim()));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return returnVal;

    }

    public List<SysRole> search(SysRole sysRole){
        try {
            return sysRoleMapper.search(sysRole);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int countSysRole(){
        return sysRoleMapper.selectCount(new SysRole());
    }


    @Transactional
    public int addRoleToAdmin(String roleIds, Integer adminId){
        try{
            deleteRoleFromAdmin(adminId);

            String[] roleIdsList = roleIds.split(",");
            for(String roleId : roleIdsList){
                if("".equals(roleId)) continue;
                sysRoleMapper.addRoleToAdmin(Integer.parseInt(roleId), adminId);
            }
            return 1;
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteRoleFromAdmin(Integer adminId){
        return sysRoleMapper.deleteRoleFromAdmin(adminId);
    }
}
