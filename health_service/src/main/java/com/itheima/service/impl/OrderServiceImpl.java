package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        OrderSetting orderSetting = orderSettingDao.findByDate((String)map.get("orderDate"));
        Date date = DateUtils.parseString2Date((String)map.get("orderDate"));
        if (orderSetting==null){
            return  new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        if (orderSetting.getNumber()<=orderSetting.getReservations()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        String idCard = (String) map.get("idCard");
        Member member = memberDao.findByIdCard(idCard);
        if (member == null) {
            member = new Member();
            registMember(map,member);
        } else {
            Order order = new Order(member.getId(), date, null, null, (Integer) map.get("setmealId"));
            List<Order> byCondition = orderDao.findByCondition(order);
            if (byCondition != null && byCondition.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //可以预约，设置预约人数加一
        orderSettingDao.addOneByDate(date);
        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        return map;
    }

    private void registMember(Map map,Member member) {
        member.setName((String)map.get("name"));
        member.setSex((String)map.get("sex"));
        member.setPhoneNumber((String)map.get("telephone"));
        member.setIdCard((String)map.get("idCard"));
        member.setRegTime(new Date());
        memberDao.registmember(member);
    }
}
