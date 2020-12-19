package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    //分页查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println("findPage~~~~~");
        System.out.println(queryPageBean.getQueryString());
        try {
            PageResult pr = setMealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @PostMapping("/addSetMeal")
    public Result addSetMeal(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        System.out.println("addSetMeal~~~~~");
        try {
            setMealService.addSetMeal(setmeal,checkGroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        System.out.println("upload~~~~~");
        try {
            int index = imgFile.getOriginalFilename().lastIndexOf(".");
            String picName = UUID.randomUUID().toString().replace("-","")+imgFile.getOriginalFilename().substring(index);
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),picName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS,picName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    @GetMapping("/deleteSetmeal")
    public Result delete(Integer id){
        System.out.println("deleteSetmeal~~~~~");
        try {
            setMealService.deleteSetmeal(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        System.out.println("findById~~~~~");
        try {
            Map map = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @PostMapping("/editSetmeal")
    public Result editSetmeal(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        System.out.println("editSetmeal~~~~~");
        try {
            setMealService.editSetmeal(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

}
