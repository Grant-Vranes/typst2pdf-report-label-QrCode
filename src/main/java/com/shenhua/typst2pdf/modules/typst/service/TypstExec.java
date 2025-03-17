package com.shenhua.typst2pdf.modules.typst.service;

import java.io.IOException;

/**
 * typst执行转换接口
 */
public interface TypstExec {

    Object typstConvertExec(Object object) throws IOException;
}
