package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void deleteCheckGroup(Integer id);

    void editCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    void addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    CheckGroup findById(Integer id);

    List<CheckItem> findAll();
}
