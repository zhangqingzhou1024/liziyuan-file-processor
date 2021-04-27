package com.liziyuan.hope.file.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.liziyuan.hope.file.core.constant.NumberConst;
import com.liziyuan.hope.file.core.exception.ExcelException;
import com.liziyuan.hope.file.core.exception.ExcelReadException;
import com.liziyuan.hope.file.core.model.*;
import com.liziyuan.hope.file.core.util.ExcelUtils;
import com.liziyuan.hope.file.core.util.FieldValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 23:37
 */
@Slf4j
@Service
public class FileProcessService {


    /**
     * 资源下载
     *
     * @param domainCode                 领域编码
     * @param resourceDownloadParameters 下载参数
     */
    public void downloadResourceMetaData(String domainCode, ResourceDownloadParameters resourceDownloadParameters) {
        // 查询满足条件的数据资源
        List<ResourceDTO> resourceList = getResourceDTO(domainCode, resourceDownloadParameters);

        // 获取excelWriter   资源模型_(域名称)_(域编码)_x个_下载时间年月日时分秒.xls
        String currentTimeStr = DateUtils.format(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
        String fileName = "资源模型_(" + domainCode + ")_" + resourceList.size() + "个_" + currentTimeStr;
        ExcelWriter excelWriter = ExcelUtils.getExcelWriter(fileName);

        // 补充说明页 & config
        ExcelUtils.initTemplateDefSheet(excelWriter);

        // 遍历资源列表生成多Sheet表格
        for (int sheetNo = 0; sheetNo < resourceList.size(); sheetNo++) {
            ResourceDTO resourceDto = resourceList.get(sheetNo);
            // 每次处理一条
            try {
                downloadOneResource(excelWriter, sheetNo + 2, resourceDto);
            } catch (Exception e) {
                log.error("生成资源元数据表格时发生异常， 资源Id:{}, 资源Code：{}, 异常信息为：{}", resourceDto.getId(), resourceDto.getCode(), e);
                throw new ExcelException("生成资源元数据表格时发生异常， 资源Id:" + resourceDto.getId() + ", 资源Code：" + resourceDto.getCode() + ", 异常信息为：" + e.getMessage());
            }
        }

        // 关闭数据流，返回excel
        excelWriter.finish();
    }


    public void uploadVerifyRuleTaskFile(String domainCode, MultipartFile file) {
        // 解析excel数据
        List<VerifyRuleTaskImportDTO> verifyRuleTaskImportList = ExcelUtils.readVerifyRuleTaskImportData(file);
        for (VerifyRuleTaskImportDTO importDTO : verifyRuleTaskImportList) {
            log.info(importDTO.toString());
        }

    }


    /**
     * 获取要下载的资源信息
     *
     * @param domainCode                 资源所属于的领域
     * @param resourceDownloadParameters 参数
     * @return 资源列表
     */
    private List<ResourceDTO> getResourceDTO(String domainCode, ResourceDownloadParameters resourceDownloadParameters) {
        List<Long> idList = resourceDownloadParameters.getIdList();
        if (FieldValueUtils.isNullType(idList)) {
            throw new ExcelException("资源Id列表不可为空 !!!");
        }

        // TODO 查询数据库 或调用接口
        List<ResourceDTO> resourceList = new ArrayList<>();
        for (Long id : idList) {
            resourceList.add(ResourceDTO.builder().id(id).code("code-" + id).name("name-" + id).modelType("pg_table").description("描述-" + domainCode).build());

        }
        return resourceList;
    }

    /**
     * 每次处理一个资源
     *
     * @param excelWriter excelWriter
     * @param sheetNo     sheetNo
     * @param resource    资源
     */
    private void downloadOneResource(ExcelWriter excelWriter, int sheetNo, ResourceDTO resource) {
        // 获取数据
        SourceDbInfoExcelBean sourceDbBean = getSourceDbBean(resource.getId());
        ResourceInfoExcelBean resourceInfoExcelBean = getResourceInfoBean(resource);
        List<ResourcePropertyExcelBean> resourcePropertyExcelBeanList = getResourcePropertyList(resource.getId());
        List<SourceDbInfoExcelBean> sourceDbInfoExcelBeans = new ArrayList<>();
        sourceDbInfoExcelBeans.add(sourceDbBean);


        List<ResourceInfoExcelBean> resourceInfoExcelBeans = new ArrayList<>();
        resourceInfoExcelBeans.add(resourceInfoExcelBean);

        // 每个资源对应一个Sheet 页
        String sheetName = getSheetName(resourceInfoExcelBean);
        log.info("sheetName :{}, resource:{}", sheetName, resourceInfoExcelBean);
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, sheetName).build();

        // 封装多个table
        WriteTable sourceDbWriteTable = EasyExcel.writerTable(0).head(SourceDbInfoExcelBean.class).build();
        WriteTable resourceWriteTable = EasyExcel.writerTable(1).head(ResourceInfoExcelBean.class).build();
        WriteTable propertyWriteTable = EasyExcel.writerTable(2).head(ResourcePropertyExcelBean.class).build();

        // 填充数据
        excelWriter.write(sourceDbInfoExcelBeans, writeSheet, sourceDbWriteTable);
        excelWriter.write(resourceInfoExcelBeans, writeSheet, resourceWriteTable);
        excelWriter.write(resourcePropertyExcelBeanList, writeSheet, propertyWriteTable);
    }

    /**
     * 获取SheetName, 最大长度不可超过31
     *
     * @param resourceInfoExcelBean 资源信息
     * @return 获取SheetName, 最大长度不可超过31
     */
    private String getSheetName(ResourceInfoExcelBean resourceInfoExcelBean) {
        String sheetName = resourceInfoExcelBean.getName() + "-(" + resourceInfoExcelBean.getCode() + ")";
        if (sheetName.length() > NumberConst.MAX_EXCEL_SHEET_NAME_LENGTH) {
            sheetName = resourceInfoExcelBean.getCode();
        }

        if (sheetName.length() > NumberConst.MAX_EXCEL_SHEET_NAME_LENGTH) {
            sheetName = UUID.randomUUID().toString().substring(10);
        }

        return sheetName;
    }

    /**
     * 获取资源自身信息
     *
     * @param resourceId 资源Id
     * @return 资源自身信息
     */
    private SourceDbInfoExcelBean getSourceDbBean(Long resourceId) {
        return SourceDbInfoExcelBean.builder().password("pwd")
                .sourceDbCode("mysql-test").sourceDbType("mysql")
                .sourceDbUrl("127.0.0.1:3306").sourceTableCode("user")
                .username("user").build();
    }


    /**
     * 获取资源自身信息
     *
     * @param resource 资源
     * @return 资源自身信息
     */
    private ResourceInfoExcelBean getResourceInfoBean(ResourceDTO resource) {
        return ResourceInfoExcelBean.builder().name(resource.getName()).
                code(resource.getCode()).description(resource.getDescription()).modelType(resource.getModelType())
                .alias(resource.getAlias()).build();

    }

    /**
     * 获取资源属性列表
     *
     * @param resourceId 资源Id
     * @return 资源属性列表
     */
    private List<ResourcePropertyExcelBean> getResourcePropertyList(Long resourceId) {
        List<ResourcePropertyExcelBean> resourceProperties = new ArrayList<>();

        for (int i = 0; i < NumberConst.THREE; i++) {
            ResourcePropertyExcelBean resourcePropertyExcelBean = ResourcePropertyExcelBean.builder().code("id").name("主键").dataType("bigserial").defaultValue("0")
                    .description("主键描述").maxLength(1).primaryKey(1).required(true).unique(true).build();

            resourceProperties.add(resourcePropertyExcelBean);
        }


        return resourceProperties;
    }
}
