package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetMealDao {

    Page<Setmeal> findPage(String queryString);

    void addSetMeal(Setmeal setmeal);

    void addCheckGroupSetMeal(Integer setmealId, Integer checkGroupId);

    Long countCheckGroupSetMealBySetMealId(Integer setmealId);

    Long countOrderBySetMealId(Integer setmealId);

    void deleteSetmeal(Integer setmealId);

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    Setmeal findSetmealById(Integer setmealId);

//edit
    void deleteGroupSetmealBySetmealId(Integer id);

    void addGroupSetmeal(@Param("setmealId") Integer setmealId,@Param("checkGroupId") Integer checkGroupId);

    void editSetmeal(Setmeal setmeal);

    List<Setmeal> findAll();

    Setmeal findSetmealDetailById(Integer setmealId);
}
