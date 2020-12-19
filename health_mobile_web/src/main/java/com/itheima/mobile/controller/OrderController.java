package com.itheima.mobile.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    JedisPool jedisPool;
    @Reference
    OrderService orderService;

    /**
     * 提交预约信息
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map orderInfo){
        String telephone = (String) orderInfo.get("telephone");
        String rediscode = jedisPool.getResource().get(telephone + RedisConstant.SENDTYPE_ORDER);
        String code = (String) orderInfo.get("validateCode");
        if(code==null||!code.equals(rediscode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        try{
            orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(orderInfo);
        }catch ( Exception e ){
            e.printStackTrace();
        }
        return  result;
    }

    @PostMapping("/findById")
    public Result findById(Integer id){
        Map order = orderService.findById(id);
        return new Result(true ,MessageConstant.QUERY_ORDER_SUCCESS,order);
    }

}
