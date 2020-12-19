package com.itheima.dao;


import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemListByGroupId(Integer groupId);

    Page<CheckItem> findPage(String queryString);

    void addCheckItem(CheckItem checkItem);

    Long countCheckItemGroupByCheckItemId(Integer id);

    void deleteCheckItem(Integer checkItemid);

    void editCheckItem(CheckItem checkItem);
}
