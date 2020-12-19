package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void addSetMeal(Setmeal setmeal, Integer[] checkGroupIds);

    void deleteSetmeal(Integer id);

    void editSetmeal(Setmeal setmeal, Integer[] checkGroupIds);

    Map findById(Integer id);

    List<Setmeal> findAll();

    Setmeal findSetmealDetailById(Integer id);
}
