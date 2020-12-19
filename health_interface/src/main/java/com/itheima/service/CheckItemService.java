package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    List<CheckItem> findAll();

    PageResult findPage(Integer currentPage,Integer pageSize,String queryString);

    void addCheckItem(CheckItem checkItem);

    void deleteCheckItem(Integer id);

    void editCheckItem(CheckItem checkItem);

//    void delete(CheckItem checkItem);
}
