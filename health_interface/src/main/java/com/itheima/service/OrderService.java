package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public interface OrderService {
    Result order(Map orderInfo) throws Exception;

    Map findById(Integer id);
}
