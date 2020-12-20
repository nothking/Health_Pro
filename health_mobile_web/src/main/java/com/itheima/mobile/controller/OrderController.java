package com.itheima.mobile.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Reference
    SetMealService setMealService;

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

    @RequestMapping("/downloadPDF")
    public Result downloadPDF (Integer id,HttpServletRequest request, HttpServletResponse response) {
        // 查询出 满足当前条件 结果数据
        // 使用订单ID，查询订单信息（需要包括套餐ID）
        Map map = orderService.findById(id);
        // 获取套餐ID
        Integer setmealId = (Integer)map.get("setmealId");
        // 使用套餐ID，查询套餐信息
        Map setmealmap = setMealService.findById(setmealId);



        return null;
    }

}
