package com.shenhua.typst2pdf.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class ResourceFileUtil {

    /**
     * 获取模板数据
     * 只能获取项目内的resources资源
     * @param relativeTemplateName 相对模板的路径 如 /static/typst_ctl/template.typ
     * @return
     * @throws IOException
     */
    public static String getTemplate(String relativeTemplateName) throws IOException {
        org.springframework.core.io.Resource resource = new ClassPathResource(relativeTemplateName);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String text = "";
        while ((line = br.readLine()) != null) {
            text += line;
        }

        br.close();
        isr.close();
        is.close();
        return text;
    }

    /**
     * 获取模板数据，每行换行
     * 只能获取项目内的resources资源
     * @param relativeTemplateName 相对模板的路径 如 /static/typst_ctl/template.typ
     * @return
     * @throws IOException
     */
    public static String getTemplateKeepLineSeparator(String relativeTemplateName) throws IOException {
        org.springframework.core.io.Resource resource = new ClassPathResource(relativeTemplateName);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String text = "";
        while ((line = br.readLine()) != null) {
            text += line + System.lineSeparator();
        }

        br.close();
        isr.close();
        is.close();
        return text;
    }

    /**
     * 获取模板数据，每行换行
     * @param absoluteTemplateName 模板的绝对路径 如 /root/springboot/typst_ctl/typst_template/template.typ
     * @return
     * @throws IOException
     */
    public static String getTemplateKeepLineSeparatorAbsolutely(String absoluteTemplateName) throws IOException {
        org.springframework.core.io.Resource resource = new FileSystemResource(absoluteTemplateName);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        String text = "";
        while ((line = br.readLine()) != null) {
            text += line + System.lineSeparator();
        }

        br.close();
        isr.close();
        is.close();
        return text;
    }

    /**
     * 通过字符串检索的方式替换数据
     * @param templateStr
     * @param params
     * @return
     */
    public static String setParams(String templateStr, Map<String, String> params){
        String text = templateStr;
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            // 防止 params.get(key)) 取出的是 null， 而导致空指针
            text = text.replaceAll("\\$\\{" + key + "}", StringUtils.isNotBlank(params.get(key)) ? params.get(key) : "");
        }
        return text;
    }

    /**
     * 通过StringSubstitutor api替换数据
     * @param templateStr
     * @param params
     * @return
     */
    public static String setParams2(String templateStr, Map<String, String> params) {
        StringSubstitutor conditionSubstitutor = new StringSubstitutor(params);
        return conditionSubstitutor.replace(templateStr);
    }
}
