package com.shenhua.typst2pdf.modules.typst.service.impl;

import com.shenhua.typst2pdf.common.utils.DatePlusUtil;
import com.shenhua.typst2pdf.common.utils.JsonUtil;
import com.shenhua.typst2pdf.modules.typst.constant.TypstConstant;
import com.shenhua.typst2pdf.modules.typst.dto.TemplateData;
import com.shenhua.typst2pdf.modules.typst.dto.TemplateRecord;
import com.shenhua.typst2pdf.modules.typst.dto.TypstExecInfo;
import com.shenhua.typst2pdf.modules.typst.service.AbstractTypstExecBase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基础模板生成pdf实现
 */
@Service("templateImpl")
public class TemplateImpl extends AbstractTypstExecBase {

    @Override
    protected TypstExecInfo queryTypstExecInfo(Object object) {
        // 转换入参
        String titleName = (String) object;

        /**
         * 可能存在的数据查询和组装
         * ...
         * 此处只传入titleName作为示例展示
         */

        /**
         * {
         *   "titleName": "titleName_f146631a3f86",
         *   "dept": "dept_0240afcdbf5",
         *   "recordData": [
         *     {
         *       "recordName": "recordName_b0240afcdbf5",
         *       "recordDescription": "recordDescription_3be0f55a88d6",
         *       "recordTime": "2025-03-15 23:26:12"
         *     }
         *   ]
         * }
         */
        TemplateData templateData = new TemplateData(titleName,
                "测试部门",
                List.of(
                        new TemplateRecord("耐久测试", "进行耐久测试...", DatePlusUtil.now()),
                        new TemplateRecord("低温测试", "进行低温测试...", DatePlusUtil.now()),
                        new TemplateRecord("高温测试", "进行高温测试...", DatePlusUtil.now()),
                        new TemplateRecord("耐腐蚀测试", "进行耐腐蚀测试...", DatePlusUtil.now()),
                        new TemplateRecord("防风测试", "进行防风测试...", DatePlusUtil.now()),
                        new TemplateRecord("压力测试", "进行压力测试...", DatePlusUtil.now()),
                        new TemplateRecord("霜冻测试", "进行霜冻测试...", DatePlusUtil.now()),
                        new TemplateRecord("震动测试", "进行震动测试...", DatePlusUtil.now()),
                        new TemplateRecord("抗衰测试", "进行抗衰测试...", DatePlusUtil.now())
                )
        );

        return new TypstExecInfo(TypstConstant.TypstAvail.TEMPLATE, JsonUtil.compressAndEscapeJson(templateData));
    }
}
