package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 源数据库表信息
 *
 * @author zqz
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(25)
public class SourceDbInfoExcelBean {
    @ExcelProperty(value = {"源数据库类型"}, index = 0)
    private String sourceDbType;
    @ExcelProperty(value = {"源数据库地址"}, index = 1)
    private String sourceDbUrl;
    @ExcelProperty(value = {"源数据库编码"}, index = 2)
    private String sourceDbCode;
    @ExcelProperty(value = {"字符串连接参数"}, index = 3)
    private String sourceDbUrlParams;
    @ExcelProperty(value = {"源表编码"}, index = 4)
    private String sourceTableCode;
    @ExcelProperty(value = {"数据库用户名"}, index = 5)
    private String username;
    @ExcelProperty(value = {"数据库密码"}, index = 6)
    private String password;
    @ExcelProperty(value = {"源数据库Pattern"}, index = 7)
    private String sourceDbPattern;
    @ExcelProperty(value = {"源表Pattern"}, index = 8)
    private String sourceTablePattern;
}
