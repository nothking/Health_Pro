package com.itheima.dao;

import com.itheima.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserDao {
    Map findByName(String username);
    User findUserByUsername(String username);
}
