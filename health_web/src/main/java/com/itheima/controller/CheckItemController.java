package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference
    CheckItemService checkItemService;

    @RequestMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAll(){
        System.out.println("findAll~~~~~");
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //分页查询
    @PostMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println("findPage~~~~~");
        System.out.println(queryPageBean.getQueryString());
        try {
            PageResult pr = checkItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * add a checkitem
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result add(@RequestBody CheckItem checkItem){
        System.out.println("add~~~~~");
        try {
            checkItemService.addCheckItem(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        System.out.println("delete~~~~~");
        try {
            checkItemService.deleteCheckItem(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @PostMapping("/alter")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result alter(@RequestBody CheckItem checkItem){
        System.out.println("alter~~~~~");
        try {
            checkItemService.editCheckItem(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

}
