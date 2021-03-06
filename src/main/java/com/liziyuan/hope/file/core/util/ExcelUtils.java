package com.liziyuan.hope.file.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.liziyuan.hope.file.core.constant.StringConst;
import com.liziyuan.hope.file.core.exception.ExcelReadException;
import com.liziyuan.hope.file.core.exception.ExcelWriteException;
import com.liziyuan.hope.file.core.model.ResourceInitSheetBean;
import com.liziyuan.hope.file.core.model.ResourceModelConfigExcelBean;
import com.liziyuan.hope.file.core.model.ResourceModelDescExcelBean;
import com.liziyuan.hope.file.core.model.VerifyRuleTaskImportDTO;
import com.liziyuan.hope.file.processor.excel.listener.BaseExcelListener;
import com.liziyuan.hope.file.processor.excel.handler.CustomBooleanConverter;
import com.liziyuan.hope.file.processor.excel.listener.VerifyRuleTaskImportDataListener;
import com.liziyuan.hope.file.processor.excel.handler.ExcelSimpleRowCellMergeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Excel??????Utils
 *
 * @author zqz
 * @version 1.0
 * @date 2021-04-22 10:27
 */
@Slf4j
public class ExcelUtils {

    /**
     * ?????????????????????????????????excel??????
     *
     * @param fileName ?????????
     * @return true: ?????????false: ?????????
     */
    public static boolean isMatchExcelFileFormat(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }

        return fileName.matches(StringConst.EXCEL_2003_FILE_NAME_PATTERN) || fileName.matches(StringConst.EXCEL_NO2003_FILE_NAME_PATTERN);
    }

    /**
     * ?????????????????????2003???excel
     *
     * @param fileName ???????????
     * @return true:2003???excel ???false: 2003???excel
     */
    public static boolean isExcel2003Format(String fileName) {
        return fileName.matches(StringConst.EXCEL_2003_FILE_NAME_PATTERN);
    }

    /**
     * ???????????? sheet ??? Excel
     *
     * @param excel       ??????
     * @param returnClass ???????????????
     * @param sheetNo     sheet ????????? ???1??????
     * @return Excel ?????? list
     */
    public static List<Object> readExcel(MultipartFile excel, Class returnClass, int sheetNo) {
        return readExcel(excel, new BaseExcelListener(), returnClass, sheetNo, 1);
    }


    /**
     * ???????????? sheet ??? Excel
     *
     * @param file          ??????
     * @param eventListener eventListener
     * @param returnClass   ????????????
     * @param sheetNo       sheet ????????? ???1??????
     * @param headRowNumber ????????????????????????
     * @return sheet ????????????
     */
    public static List<Object> readExcel(MultipartFile file, AnalysisEventListener eventListener, Class returnClass, int sheetNo, int headRowNumber) {
        if (null == file) {
            throw new ExcelReadException("");
        }
        String fileName = file.getOriginalFilename();
        log.info("ExcelUtil.readExcel ???????????? =???{}", fileName);
        if (FieldValueUtils.isNullType(fileName)) {
            throw new ExcelReadException("????????????????????????");
        }
        boolean isExcelFileFormat = ExcelUtils.isMatchExcelFileFormat(fileName);
        if (!isExcelFileFormat) {
            throw new ExcelReadException("Excel??????????????????");
        }

        try {
            boolean isExcel2003 = ExcelUtils.isExcel2003Format(fileName);
            InputStream is = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(is).excelType(isExcel2003 ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX);
            ExcelReader excelReader = excelReaderBuilder.build();
            List<ReadSheet> readSheetList = excelReader.excelExecutor().sheetList();
            if (CollectionUtils.isEmpty(readSheetList)) {
                log.warn("ExcelUtil.readExcel ????????????????????? sheet??????");
                return Collections.emptyList();
            }
            log.info("ExcelUtil.readExcel ??????sheet???=???{}", readSheetList.size());
            return EasyExcelFactory.read(file.getInputStream()).excelType(isExcel2003 ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX).sheet(sheetNo).headRowNumber(headRowNumber).registerReadListener(eventListener).head(returnClass).doReadSync();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ExcelUtil.readExcel ????????????,???????????????{}", e.getMessage());
            throw new ExcelReadException("ExcelUtils.readExcel ????????????,???????????????" + e.getMessage());
        }
    }


    /**
     * ?????????????????????????????????
     *
     * @param file excel ??????
     * @return ?????????????????????????????????
     */
    public static List<VerifyRuleTaskImportDTO> readVerifyRuleTaskImportData(MultipartFile file) {
        try {
            List<Object> objects = readExcel(file, new VerifyRuleTaskImportDataListener(), VerifyRuleTaskImportDTO.class, 0, 1);
            if (CollectionUtils.isEmpty(objects)) {
                throw new ExcelReadException("Excel??????????????????");
            }
            log.info("ExcelUtil.readVerifyRuleTaskImportData ??????????????????=???{}", objects.size());

            // ????????????
            return ModelMapperUtils.mapList(objects, VerifyRuleTaskImportDTO.class);
        } catch (Exception e) {
            log.error("ExcelUtil.readVerifyRuleTaskImportData ????????????,???????????????{}", e.getMessage());
            throw e;
        }
    }

    /**
     * ?????? ExcelWriter
     *
     * @return ExcelWriter
     */
    public static ExcelWriter getExcelWriter(String fileName) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = ExcelUtils.getCellStyleStrategy();
            OutputStream outputStream = ExcelUtils.getExcelOutputStream(fileName, response);
            return EasyExcel.write(outputStream).registerConverter(new CustomBooleanConverter())
                    .registerWriteHandler(horizontalCellStyleStrategy).build();
        } catch (Exception e) {
            throw new ExcelWriteException("?????????Excel?????????(excelWriter)???????????????????????????????????? " + e.getMessage());
        }

    }

    /**
     * ????????????Excel???
     *
     * @param fileName ?????????
     * @param response response
     * @return
     */
    public static OutputStream getExcelOutputStream(String fileName, HttpServletResponse response) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    public static HorizontalCellStyleStrategy getCellStyleStrategy() {
        // ??????
        WriteFont writeFont = new WriteFont();
        writeFont.setFontName("????????????");
        writeFont.setFontHeightInPoints((short) 14);
        writeFont.setBold(true);

        // ??????
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWriteFont(writeFont);
        headWriteCellStyle.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());

        // ???????????????
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("????????????");
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteFont.setBold(false);

        contentWriteCellStyle.setWriteFont(contentWriteFont);

        //?????????????????? = ?????????
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        contentWriteCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        contentWriteCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        contentWriteCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // ??????????????? ?????????????????? ???????????????????????? ?????????????????????????????????
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * ????????????sheet??? ??????
     *
     * @param sheetNo ?????????no
     * @return ??????sheet??? ??????
     */
    public static List<LinkedHashMap<Integer, String>> getResourceTemplateSheetDataList(int sheetNo) {
        InputStream resourceInputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/ResourceModelDownloadTemplate.xlsx");
            resourceInputStream = classPathResource.getInputStream();
            return EasyExcelFactory.read(resourceInputStream).excelType(ExcelTypeEnum.XLSX).sheet(sheetNo).headRowNumber(0).registerReadListener(new BaseExcelListener()).doReadSync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resourceInputStream != null) {
                try {
                    resourceInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /**
     * ???????????????????????????
     *
     * @param excelWriter excelWriter
     */
    public static void initTemplateDefSheet(ExcelWriter excelWriter) {
        try {
            ExcelUtils.initTemplateDescSheet(excelWriter, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ExcelUtils.initTemplateConfigSheet(excelWriter, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ???????????????
     *
     * @param excelWriter excelWriter
     */
    private static void initTemplateDescSheet(ExcelWriter excelWriter, int sheetNo) {
        List<LinkedHashMap<Integer, String>> sheetDataList = getResourceTemplateSheetDataList(sheetNo);
        if (FieldValueUtils.isNullType(sheetDataList)) {
            return;
        }

        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, "?????????").build();
        writeSheet.setCustomWriteHandlerList(getDescSheetMergeStrategyList());

        List<ResourceInitSheetBean> initSheetTableBeanList = new ArrayList<>(6);
        initSheetTableBeanList.add(ResourceInitSheetBean.builder().tableNo(0).tableTitle(getTableTitle(new String[]{"Sheet??????", ""})).headClazz(ResourceModelDescExcelBean.class).needHead(true).startRow(1).endRow(5).build());
        initSheetTableBeanList.add(ResourceInitSheetBean.builder().tableNo(1).tableTitle(getTableTitle(new String[]{"??????????????????", ""})).headClazz(ResourceModelDescExcelBean.class).needHead(true).startRow(6).endRow(11).build());
        initSheetTableBeanList.add(ResourceInitSheetBean.builder().tableNo(2).tableTitle(getTableTitle(new String[]{"????????????????????????", ""})).headClazz(ResourceModelDescExcelBean.class).needHead(true).startRow(12).endRow(25).build());
        initSheetTableBeanList.add(ResourceInitSheetBean.builder().tableNo(3).tableTitle(getTableTitle(new String[]{"????????????????????????", ""})).headClazz(ResourceModelDescExcelBean.class).needHead(true).startRow(26).endRow(28).build());
        initSheetTableBeanList.add(ResourceInitSheetBean.builder().tableNo(4).tableTitle(getTableTitle(new String[]{"????????????????????????", ""})).headClazz(ResourceModelDescExcelBean.class).needHead(true).startRow(29).endRow(34).build());

        for (ResourceInitSheetBean table : initSheetTableBeanList) {
            WriteTable writeTable = EasyExcel.writerTable(table.getTableNo()).head(table.getHeadClazz()).head(table.getTableTitle()).needHead(table.isNeedHead()).build();
            List<LinkedHashMap<Integer, String>> sheetDtList = sheetDataList.subList(table.getStartRow(), table.getEndRow());
            List<ResourceModelDescExcelBean> dataList = new ArrayList<>();
            for (LinkedHashMap<Integer, String> map : sheetDtList) {
                ResourceModelDescExcelBean descExcelBean = ResourceModelDescExcelBean.builder().descKey(map.get(0))
                        .descVal(map.get(1)).build();

                dataList.add(descExcelBean);
            }
            excelWriter.write(dataList, writeSheet, writeTable);
        }
    }

    /**
     * ???????????????
     *
     * @param excelWriter excelWriter
     */
    private static void initTemplateConfigSheet(ExcelWriter excelWriter, int sheetNo) {
        List<LinkedHashMap<Integer, String>> sheetDataList = getResourceTemplateSheetDataList(sheetNo);
        if (FieldValueUtils.isNullType(sheetDataList)) {
            return;
        }
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, "config").build();
        WriteTable writeTable = EasyExcel.writerTable(0).head(ResourceModelConfigExcelBean.class).build();

        List<ResourceModelConfigExcelBean> configExcelBeans = new ArrayList<>();
        for (LinkedHashMap<Integer, String> dataList : sheetDataList) {
            try {
                ResourceModelConfigExcelBean modelConfigExcelBean = ResourceModelConfigExcelBean.builder().dbType(dataList.get(0)).resourceType(dataList.get(1))
                        .dataType(dataList.get(2)).build();

                configExcelBeans.add(modelConfigExcelBean);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        excelWriter.write(configExcelBeans.subList(1, configExcelBeans.size() - 1), writeSheet, writeTable);
    }

    /**
     * ????????????title
     *
     * @param titles ??????
     * @return ??????title
     */
    public static List<List<String>> getTableTitle(String[] titles) {
        List<List<String>> modelInfoTitleRow = new ArrayList<>();
        Arrays.asList(titles).forEach(title -> modelInfoTitleRow.add(Collections.singletonList(title)));
        return modelInfoTitleRow;
    }

    /**
     * ??????Sheet ????????? ???????????????
     *
     * @return ???????????????????????????
     */
    public static List<WriteHandler> getDescSheetMergeStrategyList() {
        List<WriteHandler> mergeStrategies = new ArrayList<>();
        List<Integer> mergeRowIndexList = new ArrayList<>();
        mergeRowIndexList.add(0);
        mergeRowIndexList.add(4);
        mergeRowIndexList.add(5);
        mergeRowIndexList.add(11);
        ExcelSimpleRowCellMergeStrategy rowCellMergeStrategy = ExcelSimpleRowCellMergeStrategy.builder()
                .mergeRowIndexList(mergeRowIndexList).startRowIndex(24).endRowIndex(28)
                .startColIndex(0).endColIndex(1).build();
        mergeStrategies.add(rowCellMergeStrategy);

        return mergeStrategies;
    }

}
