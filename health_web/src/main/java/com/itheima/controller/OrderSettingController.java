package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.OrderSettingService;
import com.itheima.util.POIUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try{
            List<String[]> strings = POIUtils.readExcel(excelFile);
            orderSettingService.addAllOrderSetting(strings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    //orderSet
    @PostMapping("/orderSet")
    public Result orderSet(@RequestParam String date,@RequestParam Integer number){
        try{
            System.out.println(date+"\t" + number);
            orderSettingService.orderSet(date,number);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    //getLeftObj
    @PostMapping("/getLeftObj")
    public Result getLeftObj(String ym){
        try{
            System.out.println(ym);
            List list = orderSettingService.getLeftObj(ym);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

}
