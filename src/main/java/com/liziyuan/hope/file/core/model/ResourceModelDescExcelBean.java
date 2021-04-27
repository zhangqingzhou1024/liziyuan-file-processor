package com.liziyuan.hope.file.core.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源模型-说明 sheet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceModelDescExcelBean {

    @ColumnWidth(25)
    @ExcelProperty(index = 0)
    private String descKey;
    @ColumnWidth(140)
    @ExcelProperty(index = 1)
    private String descVal;
}
