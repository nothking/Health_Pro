package com.itheima.service;

import java.util.List;

public interface OrderSettingService {
    void addAllOrderSetting(List<String[]> strings);

    void orderSet(String date, Integer number);

    List getLeftObj(String ym);
}
