package com.itheima.dao;

import com.itheima.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleDao {
    Role findByUserId(Integer userId);

    Set<Role> findRolesByUserId(Integer userId);
}
