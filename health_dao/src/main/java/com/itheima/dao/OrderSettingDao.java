package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
@Repository
public interface OrderSettingDao {

    Set<String> findAllDate();

    void editOrderSettingByDate(@Param("orderDate")String orderDate, @Param("number") Integer number);

    void addOrderSetting(@Param("orderDate") String orderDate, @Param("number") Integer number);

    Long countOrderSettingByDate(String date);

    List<OrderSetting> findByYM(String ym);

    OrderSetting findByDate(String orderDate);

    void addOneByDate(Date date);
}
