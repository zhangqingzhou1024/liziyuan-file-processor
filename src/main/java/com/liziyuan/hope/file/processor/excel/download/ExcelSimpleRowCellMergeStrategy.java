package com.liziyuan.hope.file.processor.excel.download;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * 简单的合并单月格
 *
 * @author zqz
 * @version 1.0
 * @date 2021-04-25 10:30
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSimpleRowCellMergeStrategy implements RowWriteHandler {
    private List<Integer> mergeRowIndexList;
    private int mergeRowIndex;
    private int startRowIndex;
    private int endRowIndex;
    private int startColIndex;
    private int endColIndex;

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        //当前行
        int curRowIndex = row.getRowNum();
        boolean isMerge = curRowIndex == mergeRowIndex || mergeRowIndexList.contains(curRowIndex) || (curRowIndex >= startRowIndex && curRowIndex <= endRowIndex);
        if (isMerge) {
            mergeWithRow(writeSheetHolder, row, curRowIndex, startColIndex, endColIndex);
        }
    }

    /**
     * 合并单元格
     *
     * @param writeSheetHolder writeSheetHolder
     * @param row              row
     * @param curRowIndex      行信息
     * @param startColIndex    列-开始
     * @param endColIndex      列-结束
     */
    private void mergeWithRow(WriteSheetHolder writeSheetHolder, Row row, int curRowIndex, int startColIndex, int endColIndex) {
        // 比较当前行的第一列的单元格与上一行是否相同，相同合并当前单元格与上一行
        Sheet sheet = writeSheetHolder.getSheet();

        CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex, curRowIndex, startColIndex, endColIndex);
        sheet.addMergedRegion(cellRangeAddress);
    }


}


