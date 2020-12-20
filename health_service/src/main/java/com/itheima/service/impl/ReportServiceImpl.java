package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    SetMealDao setMealDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-11);
        Map map = new HashMap();
        List<String> months =new ArrayList<>();
        List<Integer> memberCounts = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String date ;

            date = calendar.get(Calendar.YEAR)+"-" +calendar.get(Calendar.MONTH);
            if (calendar.get(Calendar.MONTH)==0){
                date = (calendar.get(Calendar.YEAR)-1)+"-12";
            }
            Integer count = memberDao.findMemberCountBeforeDate(date+"-31");
            months.add(date);
            memberCounts.add(count);
            calendar.add(Calendar.MONTH,1);
        }
        map.put("months",months);
        map.put("memberCounts",memberCounts);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @Override
    public Result getSetmealReport() {

        List<Map> setmealCounts = setMealDao.countSetmeal();
        List<String> setmealNames = new ArrayList<>();
//        setmealCounts.forEach(sc ->sc.keySet().forEach(name -> setmealNames.add((String) name)));

        for (Map setmealCount : setmealCounts) {
            setmealNames.add((String) setmealCount.get("name"));
        }
        Map rsResult = new HashMap();
        rsResult.put("setmealNames",setmealNames);
        rsResult.put("setmealCount",setmealCounts);
        return  new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,rsResult);
    }

    @Override
    public Map getBusinessReportData() throws Exception {
        Map rsMap = new HashMap();
        /**
         *                 reportDate: null,
         *                 todayNewMember: 0,
         *                 totalMember: 0,
         *                 thisWeekNewMember: 0,
         *                 thisMonthNewMember: 0,
         *                 todayOrderNumber: 0,
         *                 todayVisitsNumber: 0,
         *                 thisWeekOrderNumber: 0,
         *                 thisWeekVisitsNumber: 0,
         *                 thisMonthOrderNumber: 0,
         *                 thisMonthVisitsNumber: 0,
         *                 hotSetmeal: [
         *                     {name: '阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐', setmeal_count: 200, proportion: 0.222},
         *                     {name: '阳光爸妈升级肿瘤12项筛查体检套餐', setmeal_count: 200, proportion: 0.222}
         *                 ]
         */
        // 获得当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获取本周最后一天的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 获取本月最后一天的日期
        String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        // 今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        // 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        // 本周预约数
        Map<String,Object> weekMap = new HashMap<String,Object>();
        weekMap.put("begin",thisWeekMonday);
        weekMap.put("end",thisWeekSunday);
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(weekMap);

        // 本月预约数
        Map<String,Object> monthMap = new HashMap<String,Object>();
        monthMap.put("begin",firstDay4ThisMonth);
        monthMap.put("end",lastDay4ThisMonth);
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);

        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        // 本周到诊数
        Map<String,Object> weekMap2 = new HashMap<String,Object>();
        weekMap2.put("begin",thisWeekMonday);
        weekMap2.put("end",thisWeekSunday);
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(weekMap2);

        // 本月到诊数
        Map<String,Object> monthMap2 = new HashMap<String,Object>();
        monthMap2.put("begin",firstDay4ThisMonth);
        monthMap2.put("end",lastDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(monthMap2);

        List<Map> hotSetmeal = setMealDao.findHotSetmeal();

        rsMap.put("reportDate",today);
        rsMap.put("todayNewMember",todayNewMember);
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);
        return rsMap;
    }

    @Override
    public void exportBusines() throws Exception {
        Map businessReportData = getBusinessReportData();


    }
}
