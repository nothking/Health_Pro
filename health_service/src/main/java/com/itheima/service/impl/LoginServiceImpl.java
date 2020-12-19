package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = LoginService.class)
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Result login(Map map) {
        String phone =(String )map.get("telephone");
        Member member = memberDao.findByTelephone(phone);
        if (member == null){
            member = new Member();
            registMember(map,member);
        }
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    private void registMember(Map map, Member member) {
        member.setPhoneNumber((String)map.get("telephone"));
        member.setRegTime(new Date());
        memberDao.registmemberByTelephone(member);
    }
}
