package com.liziyuan.hope.file.processor.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.liziyuan.hope.file.core.model.VerifyRuleTaskImportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 模板的读取类
 *
 * @author zqz
 */
public class VerifyRuleTaskImportDataListener extends AnalysisEventListener<VerifyRuleTaskImportDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyRuleTaskImportDataListener.class);

    List<VerifyRuleTaskImportDTO> list = new ArrayList<VerifyRuleTaskImportDTO>();

    @Override
    public void invoke(VerifyRuleTaskImportDTO data, AnalysisContext context) {
        if (context.getCurrentRowNum() > 1) {
            list.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        LOGGER.info("存储数据库成功！");
    }
}
