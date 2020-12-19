package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    JedisPool jedisPool;
    @Reference
    LoginService loginService;

    @RequestMapping("/check")
    public Result check(@RequestBody Map map){

        String telephone = (String) map.get("telephone");
        String rediscode = jedisPool.getResource().get(telephone + RedisConstant.SENDTYPE_ORDER);
        String code = (String) map.get("validateCode");
        if(code==null||!code.equals(rediscode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        try{
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = loginService.login(map);
        }catch ( Exception e ){
            e.printStackTrace();
        }
        return  result;
    }
}
