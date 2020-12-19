package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
//    @Autowired
//    private JedisPool jedisPool;

    /**
     * 批量插入设置预定表单
     * @param strings
     */
    @Override
    public void addAllOrderSetting(List<String[]> strings) {

        Set<String> s1 = orderSettingDao.findAllDate();
        final boolean flag = s1!=null && s1.size()>0;
        for (int i = 0; i < strings.size(); i++) {
            if( flag && s1.contains(strings.get(i)[0])){
                orderSettingDao.editOrderSettingByDate(strings.get(i)[0],Integer.getInteger(strings.get(i)[1]));
                continue;
            }
            orderSettingDao.addOrderSetting(strings.get(i)[0],Integer.parseInt(strings.get(i)[1]));
        }
    }

    @Override
    public void orderSet(String date, Integer number) {
        Long count = orderSettingDao.countOrderSettingByDate(date);
        if (count>0){
            orderSettingDao.editOrderSettingByDate(date,number);
            return;
        }
        orderSettingDao.addOrderSetting(date,number);
    }

    @Override
    public List getLeftObj(String ym) {
        List list = new ArrayList();
        List<OrderSetting> orderSettings = orderSettingDao.findByYM(ym);
        if (orderSettings==null || orderSettings.size()==0){
            return null;
        }
        for (OrderSetting orderSetting : orderSettings) {
            Map map = new HashMap();
            map.put("date",orderSetting.getOrderDate().toString().split(" ")[2]);
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            list.add(map);
        }
        return list;
    }
}
