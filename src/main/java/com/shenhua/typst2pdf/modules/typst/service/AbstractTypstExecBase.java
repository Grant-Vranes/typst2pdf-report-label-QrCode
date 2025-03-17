package com.shenhua.typst2pdf.modules.typst.service;

import com.shenhua.typst2pdf.common.utils.FileUtil;
import com.shenhua.typst2pdf.common.utils.ProcessUtil;
import com.shenhua.typst2pdf.common.utils.ResourceFileUtil;
import com.shenhua.typst2pdf.common.utils.UUIDUtils;
import com.shenhua.typst2pdf.modules.typst.constant.TypstConstant;
import com.shenhua.typst2pdf.modules.typst.dto.TypstExecInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @description:
 * @author：
 * @date: 2024/4/15 上午10:32
 * @Copyright： 
 */
public abstract class AbstractTypstExecBase implements TypstExec {
    private Logger LOG = LoggerFactory.getLogger(AbstractTypstExecBase.class);

    @Value("${typst.exec-0.10.0}")
    private String typstExec_0_10_0;
    @Value("${typst.exec-0.13.0}")
    private String typstExec_0_13_0;
    @Value("${typst.template-location}")
    private String templateLocation;
    @Value("${typst.convert-folder}")
    private String convertFolder;
    @Value("${typst.ttf}")
    private String ttf;

    /**
     * typst转换pdf的，单个
     * @param object
     * @return
     * @throws IOException
     */
    @Override
    public Object typstConvertExec(Object object) throws IOException {
        return typstDo(queryTypstExecInfo(object));
    }

    protected abstract TypstExecInfo queryTypstExecInfo(Object object);

    /**
     * typst工具执行
     * @param typstExecInfo
     * @return
     * @throws IOException
     */
    private Object typstDo(TypstExecInfo typstExecInfo) throws IOException {
        // typst模式
        TypstConstant.TypstAvail typstAvail = typstExecInfo.getTypstAvail();

        // 封装参数，用于替换模板
        Map<String, String> params = new HashMap<>();
        params.put(typstAvail.getPlaceHolderJsonCode(), typstExecInfo.getCompressAndEscapeJson());

        // 获取对应模板，然后进行参数替换
        String printCoupletTemplate = ResourceFileUtil.getTemplateKeepLineSeparatorAbsolutely(
                FileUtil.concatenateWithSlash(templateLocation, typstAvail.getTypstTemplateName()));
        String printCoupletTemplateTypstText = ResourceFileUtil.setParams2(printCoupletTemplate, params);

        String sameUuid = UUIDUtils.uuid();
        // 将这个文件写到转换文件池中
        String typFilePath = FileUtil.concatenateWithSlash(convertFolder, typstAvail.getSourceFilePerfix() + sameUuid + TypstConstant.TYPST_SUFFIX);
        FileUtil.createFile(typFilePath, printCoupletTemplateTypstText);

        // 通过外部命令typst生成pdf到转换文件池
        // pdf临时存放绝对路径
        String finalPdfFilePath = FileUtil.concatenateWithSlash(convertFolder, typstAvail.getTargetFilePerfix() + sameUuid + TypstConstant.PDF_SUFFIX);

        /**
         * https://github.com/typst/typst
         * typst compile path/to/source.typ path/to/output.pdf
         */
        LinkedList<String> cmd = new LinkedList<>();
        // 根据程序所需版本不同，选择不同执行器
        switch (typstAvail.getTypstVersion()) {
            case V_0_10_0:
                cmd.add(typstExec_0_10_0);
                break;
            case V_0_13_0:
                cmd.add(typstExec_0_13_0);
                break;
            default:
                cmd.add(typstExec_0_10_0);
        }
        cmd.add("compile");
        cmd.add("--font-path");
        cmd.add(ttf);
        cmd.add(typFilePath);
        cmd.add(finalPdfFilePath);
        ProcessUtil.exec(cmd);

        try {
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(finalPdfFilePath);
            return FileUtil.downloadFileForInline(fileInputStream, typstAvail.getTargetFilePerfix() + sameUuid + TypstConstant.PDF_SUFFIX);
        } catch(FileNotFoundException e){
            LOG.error("FILE_NOT_FOUND => {}", e.toString());
            throw new RuntimeException("FILE_NOT_FOUND");
        }catch (IOException e) {
            LOG.error("FILE_TRANSFER_ERROR => {}", e.toString());
            throw new RuntimeException("FILE_TRANSFER_ERROR");
        }
    }
}
