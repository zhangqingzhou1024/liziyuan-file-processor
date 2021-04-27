package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源模型-config sheet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceModelConfigExcelBean {
    @ColumnWidth(25)
    @ExcelProperty(value = {"数据库类型"}, index = 0)
    private String dbType;
    @ColumnWidth(25)
    @ExcelProperty(value = {"资源类型"}, index = 1)
    private String resourceType;
    @ColumnWidth(80)
    @ExcelProperty(value = {"数据类型\n" +
            "（适配的资源类型包括：pg_table、pg_query_table、ts_table、general_file、bim_model_file）"}, index = 2)
    private String dataType;
}
