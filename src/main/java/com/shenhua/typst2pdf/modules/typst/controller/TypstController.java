package com.shenhua.typst2pdf.modules.typst.controller;

import com.shenhua.typst2pdf.modules.typst.dto.PrintLocation;
import com.shenhua.typst2pdf.modules.typst.service.TypstExec;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Authoer GV
 * @Date 2024/01/28
 * @Description
 */
@RestController
@RequestMapping("/typst")
public class TypstController {

    @Resource
    private TypstExec templateImpl;
    @Resource
    private TypstExec coupletTemplateImpl;

    /**
     * 基本模板打印
     * @param titleName 标题名
     * @return
     */
    @GetMapping("/typst-to-pdf/template")
    public Object getPermitPDF(HttpServletResponse response, @RequestParam("titleName") String titleName) throws IOException {
        return templateImpl.typstConvertExec(titleName);
    }


    /**
     * 获取二维码打印联
     * @return
     * @throws IOException
     */
    @GetMapping("/typst-to-pdf/couplet-template")
    public Object getPrintCoupletForTrial(HttpServletResponse response,
                                          @RequestParam("dataBottom") Double dataBottom,
                                          @RequestParam("dataLeft") Double dataLeft) throws IOException {
        PrintLocation printLocation = new PrintLocation(dataBottom, dataLeft);
        return coupletTemplateImpl.typstConvertExec(printLocation);
    }

}
