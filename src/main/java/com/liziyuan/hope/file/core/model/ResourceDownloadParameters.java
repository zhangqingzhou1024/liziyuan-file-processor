package com.liziyuan.hope.file.core.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-26 21:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDownloadParameters implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("searchKey")
    private String searchKey;
    @JsonProperty("idList")
    @Valid
    private List<Long> idList = null;
    @JsonProperty("isAll")
    private Boolean isAll;
}

