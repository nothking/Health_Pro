package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;
    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }



    @Override
    public void deleteCheckGroup(Integer checkGroupid) {
        Long c1 = checkGroupDao.countCheckItemGroupByCheckGroupId(checkGroupid);
        Long c2 = checkGroupDao.countSetmealCheckGroupByCheckGroupId(checkGroupid);
        if (c1 + c2 >0){
            throw new RuntimeException(MessageConstant.DELETE_CHECKGROUP_FAIL_MESSAGE);
        }
        checkGroupDao.deleteCheckGroup(checkGroupid);
    }

    @Override
    public void editCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        Set<Integer> set = new HashSet<>();
        Collections.addAll(set,checkitemIds);
        List<Integer> checkItemIdsByGroupId = checkGroupDao.findCheckItemIdsByGroupId(checkGroup.getId());
        if (!compareTwoIntegerArrays(set,checkItemIdsByGroupId)){
            checkGroupDao.deleteCheckItemGroupByGroupId(checkGroup.getId());
            addCheckItemGroup(checkGroup.getId(),checkitemIds);
        }
        checkGroupDao.editCheckGroup(checkGroup);
    }

    public Boolean compareTwoIntegerArrays(Set set,List<Integer> arr){
        if(set.size()!=arr.size())
            return false;
        return set.containsAll(arr);
    }


    @Override
    public void addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.addCheckGroup(checkGroup);
        System.out.println("id =\t"+checkGroup.getId());
        addCheckItemGroup(checkGroup.getId(),checkitemIds);
    }

    @Override
    public CheckGroup findById(Integer id) {
        List<Integer> checkItemIds = checkGroupDao.findCheckItemIdsByGroupId(id);
        CheckGroup checkGroup = checkGroupDao.findById(id);
        List<CheckItem> list = new ArrayList<>();
        for (Integer checkItemId : checkItemIds) {
            CheckItem checkItem = new CheckItem();
            checkItem.setId(checkItemId);
            list.add(checkItem);
        }
        checkGroup.setCheckItems(list);
        return checkGroup;
    }

    @Override
    public List<CheckItem> findAll() {
        return checkGroupDao.findAll();
    }

    public void addCheckItemGroup(Integer checkGroupid,Integer[] checkitemIds){
        for (Integer checkitemId : checkitemIds) {
            checkGroupDao.addCheckItemGroup(checkGroupid,checkitemId);
        }
    }
}
