package com.shenhua.typst2pdf.modules.typst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 打印联 位置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrintLocation {

    private Double dateBottom;
    private Double dateLeft;
}
