package com.liziyuan.hope.file.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 初始化模版参数传递
 *
 * @author zqz
 * @version 1.0
 * @date 2021-04-23 17:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInitSheetBean {
    int tableNo;
    Class headClazz;
    List<List<String>> tableTitle;
    int startRow;
    int endRow;
   boolean needHead;
}
