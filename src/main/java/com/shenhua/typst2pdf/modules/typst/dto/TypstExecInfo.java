package com.shenhua.typst2pdf.modules.typst.dto;

import com.shenhua.typst2pdf.modules.typst.constant.TypstConstant;
import lombok.Data;

/**
 * @description:
 * @author：
 * @date: 2024/4/15 上午10:36
 * @Copyright： 
 */
@Data
public class TypstExecInfo {

    /**
     * 模板名称和模板中的填充内容占位符
     */
    private TypstConstant.TypstAvail typstAvail;

    /**
     * 封装的参数，需要用来替换.typ模板中的数据内容
     * 使用JsonUtil.compressAndEscapeJson(Object)方法压缩转义后的json
     */
    private String compressAndEscapeJson;

    public TypstExecInfo() {
    }

    public TypstExecInfo(TypstConstant.TypstAvail typstAvail, String compressAndEscapeJson) {
        this.typstAvail = typstAvail;
        this.compressAndEscapeJson = compressAndEscapeJson;
    }
}
