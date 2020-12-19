package com.itheima.service;

import com.itheima.pojo.Member;

public interface MemberService {
    Member findByPhone(String phone);
}
