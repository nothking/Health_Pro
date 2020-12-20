package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface ReportService {
    Result getMemberReport();

    Result getSetmealReport();

    Map getBusinessReportData() throws Exception;

    void exportBusines() throws Exception;
}
