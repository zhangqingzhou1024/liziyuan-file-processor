package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检核任务批量导入实体信息
 *
 * @author zqz
 * @version 1.0
 * @date 2021-01-06 15:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRuleTaskImportDTO {
    @ExcelProperty(value = {"检核名称"}, index = 0)
    private String verifyName;
    @ExcelProperty(value = {"检核编码"}, index = 1)
    private String verifyCode;
    @ExcelProperty(value = {"检核分类"}, index = 2)
    private String verifyClassifyName;
    @ExcelProperty(value = {"检核条件"}, index = 3)
    private String otherParam;
    @ExcelProperty(value = {"资源编码"}, index = 4)
    private String resourceCode;
    @ExcelProperty(value = {"属性编码"}, index = 5)
    private String resourceColumnCode;
    @ExcelProperty(value = {"权重"}, index = 6)
    private Integer weight;
    @ExcelProperty(value = {"描述"}, index = 7)
    private String description;
    @ExcelProperty(value = {"调度cron表达式"}, index = 8)
    private String jobCron;
}
