package com.shenhua.typst2pdf.common.utils;

import java.io.IOException;
import java.util.List;

/**
 * Java调用外部可执行程序或系统命令
 */
public class ProcessUtil {

    /**
     *
     * @param cmdParams ["java", "-jar", "a.jar"]
     */
    public static void exec(List<String> cmdParams) {
        ProcessBuilder processBuilder = new ProcessBuilder(cmdParams);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("ProcessUtil exitCode = " + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
