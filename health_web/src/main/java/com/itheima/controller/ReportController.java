package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import net.sf.jxls.transformer.Workbook;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        return reportService.getMemberReport();
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        return reportService.getSetmealReport();
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() throws Exception {
        Map map = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

    @RequestMapping("/exportBusines")
    public Result exportBusines(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map result = reportService.getBusinessReportData();
        //取出返回结果数据，准备将报表数据写入到Excel文件中
//        String reportDate = (String) result.get("reportDate");
//        Integer todayNewMember = (Integer) result.get("todayNewMember");
//        Integer totalMember = (Integer) result.get("totalMember");
//        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
//        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
//        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
//        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
//        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
//        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
//        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
//        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
//        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        //获得Excel模板文件绝对路径
        String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                File.separator + "report_template.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(temlateRealPath));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformWorkbook(workbook,result);
        OutputStream os = response.getOutputStream();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
        return  null;
    }
}
