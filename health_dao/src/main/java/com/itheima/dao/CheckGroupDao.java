package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckGroupDao {

    List<CheckGroup> findCheckGroupListBySetmealId(Integer setmealId);

    List<CheckItem> findAll();

    Page<CheckGroup> findPage(String queryString);

    void addCheckGroup(CheckGroup checkGroup);

    void addCheckItemGroup(@Param("checkGroupid") Integer checkGroupid, @Param("checkitemId") Integer checkitemId);

    /**
     * 以下三个方法为删除CheckGroup
     */
    Long countCheckItemGroupByCheckGroupId(Integer checkGroupid);

    Long countSetmealCheckGroupByCheckGroupId(Integer checkGroupid);

    void deleteCheckGroup(Integer checkGroupid);

    List<Integer> findCheckItemIdsByGroupId(Integer id);

    CheckGroup findById(Integer checkGroupId);

    void deleteCheckItemGroupByGroupId(Integer checkGroupId);

    void editCheckGroup(CheckGroup checkGroup);


}
