package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.liziyuan.hope.file.processor.excel.handler.CustomBooleanConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(25)
public class ResourcePropertyExcelBean {

    @ExcelProperty(value = {"属性名"}, index = 0)
    private String name;

    @ExcelProperty(value = {"属性代码"}, index = 1)
    private String code;

    @ExcelProperty(value = {"属性类型"}, index = 2)
    private String dataType;

    @ExcelProperty(value = {"不能为空"}, index = 3, converter = CustomBooleanConverter.class)
    private Boolean required;

    @ExcelProperty(value = {"是否唯一"}, index = 4, converter = CustomBooleanConverter.class)
    private Boolean unique;

    @ExcelProperty(value = {"主键顺序"}, index = 5)
    private Integer primaryKey;

    @ExcelProperty(value = {"最大长度"}, index = 6)
    private Integer maxLength;

    @ExcelProperty(value = {"默认值"}, index = 7)
    private String defaultValue;

    @ExcelProperty(value = {"属性描述"}, index = 8)
    private String description;

}
