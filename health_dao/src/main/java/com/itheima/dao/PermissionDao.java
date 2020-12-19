package com.itheima.dao;

import com.itheima.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionDao {
    Permission findByRoleId(Integer roleId);
    //findPermissionsByRoleId
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
