package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    CheckGroupService checkGroupService;

    @RequestMapping("/findAll")
    public Result findAll(){
        System.out.println("findAll~~~~~");
        try {
            List<CheckItem> list = checkGroupService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    //分页查询
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println("findPage~~~~~");
        System.out.println(queryPageBean.getQueryString());
        try {
            PageResult pr = checkGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
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
    @PostMapping("/addCheckGroup")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        System.out.println("add~~~~~");
        try {
            checkGroupService.addCheckGroup(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        System.out.println("delete~~~~~");
        try {
            checkGroupService.deleteCheckGroup(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        System.out.println("findById~~~~~");
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @PostMapping("/editCheckGroup")
    public Result editCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        System.out.println("editCheckGroup~~~~~");
        try {
            checkGroupService.editCheckGroup(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
}
