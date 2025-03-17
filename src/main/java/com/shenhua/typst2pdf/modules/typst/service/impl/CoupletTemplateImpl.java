package com.shenhua.typst2pdf.modules.typst.service.impl;

import com.shenhua.typst2pdf.common.utils.JsonUtil;
import com.shenhua.typst2pdf.modules.typst.constant.TypstConstant;
import com.shenhua.typst2pdf.modules.typst.dto.CoupletTemplateData;
import com.shenhua.typst2pdf.modules.typst.dto.PrintLocation;
import com.shenhua.typst2pdf.modules.typst.dto.TypstExecInfo;
import com.shenhua.typst2pdf.modules.typst.service.AbstractTypstExecBase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板联生成pdf实现
 * 根据传入list打印多页
 * 可用于打印标签，二维码等
 * 可标定位置(此为展示typst的模板灵活的可行性)
 */
@Service("coupletTemplateImpl")
public class CoupletTemplateImpl extends AbstractTypstExecBase {

    @Override
    protected TypstExecInfo queryTypstExecInfo(Object object) {
        // 先转换
        PrintLocation printLocation = (PrintLocation) object;

        // 填充
        CoupletTemplateData coupletTemplateData = new CoupletTemplateData();
        coupletTemplateData.setQrList(List.of("https://baidu.com", "https://baidu.com"));
        coupletTemplateData.setLocation(printLocation);

        return new TypstExecInfo(TypstConstant.TypstAvail.COUPLET_TEMPLATE, JsonUtil.compressAndEscapeJson(coupletTemplateData));
    }
}