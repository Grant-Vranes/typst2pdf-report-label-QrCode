package com.shenhua.typst2pdf.modules.typst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRecord {

    private String recordName;
    private String recordDescription;
    private LocalDateTime recordTime;
}
