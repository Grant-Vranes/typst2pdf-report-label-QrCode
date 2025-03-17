package com.shenhua.typst2pdf.modules.typst.constant;

public class TypstConstant {

    /**
     * PDF文件后缀
     */
    public static final String PDF_SUFFIX = ".pdf";

    /**
     * typst文件后缀
     */
    public static final String TYPST_SUFFIX = ".typ";

    /**
     * typst对应的模板和其中的填充字段占位符
     */
    public enum TypstAvail {
        /**
         * 模板
         */
        TEMPLATE("template.typ",
                "templateJson",
                "template_",
                "template_pdf_",
                TypstVersion.V_0_10_0),

        /**
         * 模板联
         */
        COUPLET_TEMPLATE("couplet_template.typ",
                "coupletTemplateJson",
                "couplet_template_",
                "couplet_template_pdf_",
                TypstVersion.V_0_10_0),
        ;

        /**
         * 模板名称, 处于配置的模板目录下
         */
        private String typstTemplateName;
        /**
         * 模板中的占位符code
         */
        private String placeHolderJsonCode;
        /**
         * .typ源文件前缀
         */
        private String sourceFilePerfix;
        /**
         * 目标文件前缀, pdf文件
         */
        private String targetFilePerfix;
        /**
         * typst的支持版本
         */
        private TypstVersion typstVersion;

        TypstAvail(String typstTemplateName,
                   String placeHolderJsonCode,
                   String sourceFilePerfix,
                   String targetFilePerfix,
                   TypstVersion typstVersion) {
            this.typstTemplateName = typstTemplateName;
            this.placeHolderJsonCode = placeHolderJsonCode;
            this.sourceFilePerfix = sourceFilePerfix;
            this.targetFilePerfix = targetFilePerfix;
            this.typstVersion = typstVersion;
        }

        public String getTypstTemplateName() {
            return typstTemplateName;
        }

        public String getPlaceHolderJsonCode() {
            return placeHolderJsonCode;
        }

        public String getSourceFilePerfix() {
            return sourceFilePerfix;
        }

        public String getTargetFilePerfix() {
            return targetFilePerfix;
        }

        public TypstVersion getTypstVersion() {
            return typstVersion;
        }
    }

    /**
     * typst的支持版本
     * https://github.com/typst/typst/releases/tag/v0.10.0
     * https://github.com/typst/typst/releases/tag/v0.13.0
     */
    public enum TypstVersion {
        V_0_10_0,
        V_0_13_0,
        ;
    }
}
