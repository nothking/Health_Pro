package com.itheima.mobile.controller;

import com.itheima.constant.AliCloudConstant;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;


@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        try{
            if (telephone==null||telephone.length()<11){
                return  new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
            }
            String code = ValidateCodeUtils.generateValidateCode4String(4);
//            SMSUtils.sendShortMessage(AliCloudConstant.SIGN_CODE,telephone, code);
            System.out.println(code);
            jedisPool.getResource().setex(telephone+ RedisConstant.SENDTYPE_ORDER,5*60,code);
            return  new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    //send4Login

    @PostMapping("/send4Login")
    public Result send4Login(String telephone) {
        try{
            if (telephone==null||telephone.length()<11){
                return  new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
            }
            String code = ValidateCodeUtils.generateValidateCode4String(4);
//            SMSUtils.sendShortMessage(AliCloudConstant.SIGN_CODE,telephone, code);
            System.out.println(code);
            jedisPool.getResource().setex(telephone+ RedisConstant.SENDTYPE_ORDER,5*60,code);
            return  new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
