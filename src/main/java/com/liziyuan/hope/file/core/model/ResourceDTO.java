package com.liziyuan.hope.file.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-26 22:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {
    private Long id;
    private String name;
    private String code;
    private String alias;
    private String description;
    private String modelType;
}
