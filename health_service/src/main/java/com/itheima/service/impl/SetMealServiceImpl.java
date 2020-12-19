package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetMealDao setMealDao;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath ;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void addSetMeal(Setmeal setmeal, Integer[] checkGroupIds) {

        setMealDao.addSetMeal(setmeal);
        if (checkGroupIds!=null && checkGroupIds.length>0)
        addCheckGroupSetMeal(setmeal.getId(),checkGroupIds);


        generateMobileStaticHtml();
    }

    private void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmeallist = setMealDao.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealStaticHtml(setmeallist);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailStaticHtml(setmeallist);
    }

    private void generateMobileSetmealStaticHtml(List<Setmeal> setmeallist) {
        Map<String, Object> map = new HashMap();
        map.put("setmealList",setmeallist);

        generateHtml("setmeal.ftl","m_setmeal.html",map);
    }

    private void generateMobileSetmealDetailStaticHtml(List<Setmeal> setmeallist) {
        Map<String,Object> map = new HashMap<>();

        for (Setmeal setmeal : setmeallist) {
            Setmeal setmealDetailById = setMealDao.findSetmealDetailById(setmeal.getId());
            map.put("setmeal",setmealDetailById);
            generateHtml("setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }

    }

    private void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try{
            Template template = configuration.getTemplate(templateName);
            File file = new File(outputpath+htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            template.process(dataMap,out);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (out!=null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void deleteSetmeal(Integer setmealId) {
        Long l1 = setMealDao.countCheckGroupSetMealBySetMealId(setmealId);
        Long l2 = setMealDao.countOrderBySetMealId(setmealId);
        if (l1+l2 >0){
            throw  new RuntimeException(MessageConstant.DELETE_SETMEAL_FAIL);
        }
        setMealDao.deleteSetmeal(setmealId);
    }

    @Override
    public void editSetmeal(Setmeal setmeal, Integer[] checkGroupIds) {
        Set<Integer> set = new HashSet<>();
        if (checkGroupIds!=null && checkGroupIds.length >0){
            Collections.addAll(set,checkGroupIds);
        }
            List<Integer> GroupIds = setMealDao.findCheckGroupIdsBySetmealId(setmeal.getId());
            if (!compareTwoCollections(set,GroupIds)){
                setMealDao.deleteGroupSetmealBySetmealId(setmeal.getId());
                addGroupSetmeal(setmeal.getId(),checkGroupIds);
            }

        setMealDao.editSetmeal(setmeal);
    }

    private boolean compareTwoCollections(Set<Integer> set, List<Integer> arr) {
        if(set.size()!=arr.size())
            return false;
        return set.containsAll(arr);
    }
    private void addGroupSetmeal(Integer setmealId, Integer[] checkGroupIds) {
        for (Integer checkGroupId : checkGroupIds) {
            setMealDao.addGroupSetmeal(setmealId,checkGroupId);
        }
    }

    @Override
    public Map findById(Integer setmealId) {
        List<Integer> list = setMealDao.findCheckGroupIdsBySetmealId(setmealId);
        Setmeal setmeal = setMealDao.findSetmealById(setmealId);
        Map map = new HashMap();
        map.put("setmeal",setmeal);
        map.put("groupIds",list);
        return map;
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = setMealDao.findAll();
        return list;
    }

    @Override
    public Setmeal findSetmealDetailById(Integer id) {
        Setmeal setmeal = setMealDao.findSetmealDetailById(id);
        return setmeal;
    }

    public void addCheckGroupSetMeal(Integer setmealId,Integer[] checkGroupIds){
        for (Integer checkGroupId : checkGroupIds) {
            setMealDao.addCheckGroupSetMeal(setmealId,checkGroupId);
        }
    }

}
