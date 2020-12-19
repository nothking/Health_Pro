package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public PageResult findPage(Integer currentPage,Integer pageSize,String queryString) {

        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page =checkItemDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public void deleteCheckItem(Integer checkItemid) {
        Long count = checkItemDao.countCheckItemGroupByCheckItemId(checkItemid);
        if (count > 0){
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_FAIL_MESSAGE);
        }
        checkItemDao.deleteCheckItem(checkItemid);
    }

    @Override
    public void editCheckItem(CheckItem checkItem) {
        checkItemDao.editCheckItem(checkItem);
    }
}
