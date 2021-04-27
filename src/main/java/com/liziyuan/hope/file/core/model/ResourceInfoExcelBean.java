package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(25)
public class ResourceInfoExcelBean {
    @ExcelProperty(value = {"目标资源名字"}, index = 0)
    private String name;
    @ExcelProperty(value = {"目标资源编码"}, index = 1)
    private String code;
    @ExcelProperty(value = {"目标资源英文名"}, index = 2)
    private String alias;
    @ExcelProperty(value = {"目标资源描述"}, index = 3)
    private String description;
    @ExcelProperty(value = {"目标资源类型"}, index = 4)
    private String modelType;
}
