package com.shenhua.typst2pdf.modules.typst.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoupletTemplateData {

    /**
     * 打印二维码联
     */
    private List<String> QrList;


    private PrintLocation location;
}
