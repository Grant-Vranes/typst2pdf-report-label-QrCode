package com.shenhua.typst2pdf.modules.typst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateData {
    private String titleName;
    private String dept;

    // 记录，用于列表展示
    private List<TemplateRecord> recordData;
}
