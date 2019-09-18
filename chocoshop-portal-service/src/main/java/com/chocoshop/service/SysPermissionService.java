package com.chocoshop.service;

import com.chocoshop.mapper.SysPermissionMapper;
import com.chocoshop.model.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionService {

    @Autowired
    SysPermissionMapper sysPermissionMapper;

    public List<SysPermission> findAll(){
        return sysPermissionMapper.selectAll();
    }
}
