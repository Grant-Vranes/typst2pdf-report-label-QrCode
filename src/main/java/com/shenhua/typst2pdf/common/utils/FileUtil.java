package com.shenhua.typst2pdf.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 */
// @Component的使用使用是因为@Value要用，不用就全部kill
@Component
public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        // 初始化一些常见的MIME类型
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("pdf", "application/pdf");
        // 添加更多需要的MIME类型...
    }

    private static final String KB_STR = "K";
    private static final String MB_STR = "M";
    private static final String GB_STR = "G";
    private static final Integer UNIT = 1024;
    private static final String FILE_SIZE_DESC_FORMAT = "%.2f";
    private static final String POINT_STR = ".";
    private static final Long ZERO_LONG = 0L;
    private static final Integer ZERO_INT = 0;
    private static final Integer ONE_INT = 1;
    private static final Integer MINUS_ONE_INT = -1;
    private static final String EMPTY_STR = "";
    /**
     * linux 或 windows环境都是支持 / 的
     */
    private static final String SLASH = "/";
    private final static String CHUNKS_FOLDER_NAME = "chunks";
    private static final String DEFAULT_ROOT_FILE_NAME = "file_temp";
    private static final String COMMON_SEPARATOR = "__,__";

    // 标准拼接符
    public static final String STANDARD_SPLICE = "-";

    /**
     * 创建临时文件
     * 这个方法将在 C:/temp 文件夹中生成一个以 "tempfile_" 为前缀，以 ".xlsx" 为后缀的临时文件
     * 使用完毕后请调用tempFile.deleteOnExit();删除这个临时文件
     * @param folderPath C:/temp
     * @param fileNamePrefix tempfile_ 前缀
     * @param fileNameSuffix .xlsx
     * @return new File("C:/temp/tempfile_*******.xlsx")
     */
    public static File generateTempFile(String folderPath, String fileNamePrefix, String fileNameSuffix) {
        // 创建一个临时文件
        File tempFile = null;
        try {
            tempFile = File.createTempFile(fileNamePrefix, fileNameSuffix, new File(folderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 删除文件，确保只有空文件被创建
        // tempFile.delete();

        return tempFile;
    }

    /**
     * 为我拼接
     * @param strs
     * @return
     */
    public static String concatenate(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 为我用斜线拼接
     * @param strs
     * @return
     */
    public static String concatenateWithSlash(String... strs) {
        // 后面可改用 File.separator， 他会自动根据运行环境识别
        return concatenateWithDelimiter(File.separator, strs);
    }

    /**
     * 为我拼接, 传入，拼接符
     * @param strs
     * @return
     */
    public static String concatenateWithDelimiter(String delimiter, String... strs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if (i < strs.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * 获取文件名，不带后缀名的那种
     * @param originalFilename 全文件名 aaa.xlsx
     * @return
     */
    public static String getFileNameWithoutExtension(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return originalFilename.substring(0, lastDotIndex);
        } else {
            return originalFilename;
        }
    }

    /**
     * 拼接文件/文件夹名称
     * @param basePath
     * @param fileOrFolderName
     * @return
     */
    public static String spliceFileOrFolderPath(String basePath, String fileOrFolderName) {
        return new StringBuffer(basePath).append(File.separator).append(fileOrFolderName).toString();
    }

    /**
     * 为路径片段附件 /
     * @param fileOrFolderName
     * @return
     */
    public static String attachFileOrFolderPath(String fileOrFolderName) {
        return new StringBuffer(File.separator).append(fileOrFolderName).toString();
    }

    /**
     * 生成临时分片文件路径
     *
     * @param chunksFolder
     * @param identifier
     * @param chunkNumber
     * @return
     */
    public static String generateChunkFilePath(String chunksFolder, String identifier, Integer chunkNumber) {
        return new StringBuffer(chunksFolder)
                .append(File.separator)
                .append(DatePlusUtil.getYear())
                .append(File.separator)
                .append(DatePlusUtil.getMonth())
                .append(File.separator)
                .append(DatePlusUtil.getDay())
                .append(File.separator)
                .append(identifier)
                .append(File.separator)
                .append(generateChunkFilename(chunkNumber)).toString();
    }

    /**
     * 生成临时分片文件名称
     *
     * @param chunkNumber
     * @return
     */
    public static String generateChunkFilename(Integer chunkNumber) {
        return new StringBuffer(UUIDUtils.uuid()).append(COMMON_SEPARATOR).append(chunkNumber).toString();
    }

    /**
     * 根据分片文件名称解析分片文件的分片号
     *
     * @param chunkFilename
     * @return
     */
    public static Integer resolveChunkFileNumber(String chunkFilename) {
        if (StringUtils.isNotBlank(chunkFilename)) {
            String chunkNumberStr = chunkFilename.substring(chunkFilename.lastIndexOf(COMMON_SEPARATOR) + COMMON_SEPARATOR.length());
            return Integer.valueOf(chunkNumberStr);
        }
        return ZERO_INT;
    }

    /**
     * 生成文件本地的保存路径
     *
     * @param filePrefix
     * @param suffix
     * @return
     */
    public static String generateFilePath(String filePrefix, String suffix) {
        return new StringBuffer(filePrefix)
                .append(File.separator)
                .append(DatePlusUtil.getYear())
                .append(File.separator)
                .append(DatePlusUtil.getMonth())
                .append(File.separator)
                .append(DatePlusUtil.getDay())
                .append(File.separator)
                .append(UUIDUtils.uuid())
                .append(suffix)
                .toString();
    }

    /**
     * 获取输入流写入输出流
     *
     * @param fileInputStream
     * @param outputStream
     * @param size
     * @throws IOException
     */
    public static void writeFileToStream(FileInputStream fileInputStream, OutputStream outputStream, Long size) throws IOException {
        FileChannel fileChannel = null;
        WritableByteChannel writableByteChannel = null;
        try {
            fileChannel = fileInputStream.getChannel();
            writableByteChannel = Channels.newChannel(outputStream);
            fileChannel.transferTo(ZERO_LONG, size, writableByteChannel);
            outputStream.flush();
        } catch (Exception e) {

        } finally {
            fileInputStream.close();
            outputStream.close();
            fileChannel.close();
            writableByteChannel.close();
        }
    }

    /**
     * 获取输入流写入输出流
     *
     * @param inputStream
     * @param targetFile
     * @param totalSize
     * @throws IOException
     */
    public static void writeStreamToFile(InputStream inputStream, File targetFile, Long totalSize) throws IOException {
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        targetFile.createNewFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        fileChannel.transferFrom(readableByteChannel, ZERO_LONG, totalSize);
        fileChannel.close();
        randomAccessFile.close();
        readableByteChannel.close();
    }

    /**
     * 传统流对流传输
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void writeStreamToStreamNormal(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != MINUS_ONE_INT.intValue()) {
            outputStream.write(buffer, ZERO_INT, len);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 获取文件大小字符串
     *
     * @param size file.getSize() 文件本来的大小
     * @return
     */
    public static String getFileSizeDesc(long size) {
        double fileSize = (double) size;
        String fileSizeSuffix = KB_STR;
        fileSize = fileSize / UNIT;
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = MB_STR;
        }
        if (fileSize >= UNIT) {
            fileSize = fileSize / UNIT;
            fileSizeSuffix = GB_STR;
        }
        return String.format(FILE_SIZE_DESC_FORMAT, fileSize) + fileSizeSuffix;
    }

    /**
     * 通过文件名获取文件后缀
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        if (StringUtils.isBlank(filename) || (filename.indexOf(POINT_STR) == MINUS_ONE_INT)) {
            return EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(POINT_STR)).toLowerCase();
    }

    /**
     * 获取文件扩展名
     *
     * @param suffix
     * @return
     */
    public static String getFileExtName(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            return suffix;
        }
        return suffix.substring(ONE_INT);
    }

    /**
     * 通过文件路径获取文件名称
     * D:\doc\示例.xlsx   --->   示例.xlsx
     *
     * @param filePath
     * @return
     */
    public static String getFilename(String filePath) {
        String filename = EMPTY_STR;
        if (StringUtils.isBlank(filePath)) {
            return filename;
        }
        if (filePath.indexOf(File.separator) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(File.separator) + ONE_INT);
        }
        if (filePath.indexOf(SLASH) != MINUS_ONE_INT) {
            filename = filePath.substring(filePath.lastIndexOf(SLASH) + ONE_INT);
        }
        return filename;
    }



    /**
     * 创建文件夹
     * 支持多级
     *
     * @param folderPath
     */
    public static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 创建文件夹, 前缀支持
     * 如prefix = /home
     * folderPath = /akio/nic
     * 注意两者拼接一定要是正常的路径
     * 支持多级
     * @param prefix
     * @param folderPath
     */
    public static void createFolder(String prefix, String folderPath) {
        File folder = new File(prefix + folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 将文件传输到对应的文件地址
     * 需要保证各级文件夹都存在
     * @param prefix /home
     * @param relativePath /akio/a.jpg
     * @param file
     */
    @Deprecated
    public static void transferTo(String prefix, String relativePath, MultipartFile file) {
        try {
            String absolutePath = prefix + relativePath;
            file.transferTo(new File(absolutePath));
        } catch (IOException e) {
            LOG.error("FILE_TRANSFER_ERROR =>{}", e.toString());
            throw new RuntimeException("FILE_TRANSFER_ERROR");
        }
    }

    /**
     * 将文件传输到对应的文件地址
     * @param prefix /home
     * @param relativePath /akio/a.jpg
     * @param file
     */
    public static void transferToAuto(String prefix, String relativePath, MultipartFile file) {
        try {
            File fileTarget = new File(prefix + relativePath);
            if (!fileTarget.getParentFile().exists()) {
                fileTarget.getParentFile().mkdirs();
            }
            file.transferTo(fileTarget);
        } catch (IOException e) {
            LOG.error("FILE_TRANSFER_ERROR =>{}", e.toString());
            throw new RuntimeException("FILE_TRANSFER_ERROR");
        }
    }

    /**
     * 如果没有文件夹就创建
     * @param path /home/a.txt 或 /home/asd/cc
     */
    public static void buildFolderIfNotExist(String path) {
        File fileTarget = new File(path);
        if (!fileTarget.getParentFile().exists()) {
            fileTarget.getParentFile().mkdirs();
        }
    }


    /**
     * 生成临时分片文件目录路径
     *
     * @param basePath
     * @return
     */
    public static String generateChunksFolderPath(String basePath) {
        return basePath + File.separator + CHUNKS_FOLDER_NAME;
    }

    /**
     * 生成临时文件目录路径
     *
     * 对于win系统  C:\Users\Administrator\a3_temp
     * 这里的a3_temp应该要提前建立
     *
     * @return
     */
    public static String generateDefaultRootFolderPath() {
        return System.getProperty("user.home") + File.separator + DEFAULT_ROOT_FILE_NAME;
    }


    /**
     * 删除物理文件、文件夹
     *
     * @param filePath
     */
    public static void delete(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        delete0(new File(filePath));
    }

    /**
     * 递归删除文件
     *
     * @param file
     * @throws IOException
     */
    private static void delete0(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    delete0(files[i]);
                }
            }
            Files.delete(file.toPath());
        } else {
            Files.delete(file.toPath());
        }
    }

    /**
     * 校验是否为空文件夹-path
     *
     * @return
     */
    public static boolean checkIsEmptyFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (!checkIsEmptyFolder(files[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 校验是否为空文件夹-File
     *
     * @return
     */
    public static boolean checkIsEmptyFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (!checkIsEmptyFolder(files[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成分片缓存key
     *
     * @param identifier
     * @param userId
     * @return
     */
    public static String generateChunkKey(String identifier, Long userId) {
        return new StringBuffer(identifier).append(COMMON_SEPARATOR).append(userId).toString();
    }

    /**
     * 创建新文件
     * 仅能用于生成文件，若已有同名文件夹，则无法生成
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static File createFile(String filePath) throws IOException {
        if (StringUtils.isNotBlank(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                return file;
            }
            createFolder(file.getParent());
            file.createNewFile();
            return file;
        }
        return null;
    }

    /**
     * 在对应位置写入一个文件，内容是content
     * @param filePath 绝对路径
     * @param content 内容
     */
    public static void createFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file, Charset.forName("UTF-8"));
                writer.write(content);
                writer.close();
            } else {
                // 文件创建失败
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加写文件
     *
     * @param target
     * @param source
     * @throws IOException
     */
    public static void appendWrite(Path target, Path source) throws IOException {
        Files.write(target, Files.readAllBytes(source), StandardOpenOption.APPEND);
    }

    /**
     * 移动物理文件
     * 类似linux中mv命令, 可实现重命名
     *
     * @param sourcePath D:\a.xlsx
     * @param targetPath D:\b\a.xlsx or D:\b\c.xlsx
     * @throws IOException
     */
    public static void moveFile(String sourcePath, String targetPath) throws IOException {
        createFile(targetPath);
        Files.move(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.ATOMIC_MOVE);
    }

    /**
     * 移动物理文件 基于prefix的基础下
     * 类似linux中mv命令, 可实现重命名
     *
     * @param prefix D:\doc
     * @param sourcePath \a.xlsx
     * @param targetPath \b\a.xlsx or \b\c.xlsx
     * @throws IOException
     */
    public static void moveFile(String prefix, String sourcePath, String targetPath) throws IOException {
        sourcePath = prefix + sourcePath;
        targetPath = prefix + targetPath;
        createFile(targetPath);
        Files.move(Paths.get(sourcePath), Paths.get(targetPath), StandardCopyOption.ATOMIC_MOVE);
    }

    /**
     * copy操作
     * @param sourcePath 如 C:\\a.txt
     * @param targetPath 如 C:\\pat\\b.txt
     */
    public static void copyTo(String sourcePath, String targetPath) {
        // 源文件路径
        Path source = Paths.get (sourcePath);
        // 目标文件路径
        Path target = Paths.get (targetPath);

        // 复制文件
        // 重命名则替换
        // Files.copy(source, destination.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        try {
            Files.copy(source, target);
        } catch (IOException e) {
            LOG.error("BUILD_FILE_DIRECTORY_IS_ABNORMAL=>{}", e.toString());
            throw new RuntimeException("BUILD_FILE_DIRECTORY_IS_ABNORMAL");
        }
    }

    /**
     * 清理目标文件夹的所有子空文件夹
     * 不能多级处理，仅能处理当前文件夹下所有空文件夹
     *
     * @param parentFolderPath
     */
    public static void cleanChildEmptyFolder(String parentFolderPath) throws IOException {
        File parentFolder = new File(parentFolderPath);
        if (parentFolder != null && parentFolder.isDirectory()) {
            File[] files = parentFolder.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory() && FileUtil.checkIsEmptyFolder(files[i])) {
                        delete(files[i].getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 清理目标文件夹的所有子空文件夹, 级联
     *
     * @param parentFolderPath
     */
    public static void cleanChildEmptyFolderCascade(String parentFolderPath) throws IOException {
        File parentFolder = new File(parentFolderPath);
        if (parentFolder != null && parentFolder.isDirectory()) {
            File[] files = parentFolder.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        if (FileUtil.checkIsEmptyFolder(files[i])) {
                            delete(files[i].getAbsolutePath());
                        } else {
                            cleanChildEmptyFolder(files[i].getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 下载文件，指定在客户机上的存放地址
     * @param urlPath     下载路径
     * @param fileSavePath 下载存放目录,包含文件名
     * @return 返回下载文件
     * @throws Exception
     */
    @Deprecated
    public static File downloadFile(String urlPath, String fileSavePath) throws Exception {

        LOG.info("File Download start ...");
        File file = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
            fileFullName = fileFullName.substring(fileFullName.lastIndexOf("/") + 1);
            LOG.info("start download" + fileFullName);
            LOG.info("file length---->" + fileLength);

            url.openConnection();

            bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = fileSavePath;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                if ((len * 100 / fileLength)>50&&(len * 100 / fileLength)<55) {
                    LOG.info("下载了-------> " + len * 100 / fileLength + "%");
                }else if ((len * 100 / fileLength)>=98) {
                    LOG.info("下载了-------> " + len * 100 / fileLength + "%");
                }
            }
            return file;
        } catch (Exception e) {
            LOG.error("FILE_NOT_FOUND => {}", e.toString());
            throw new RuntimeException("FILE_NOT_FOUND");
        } finally {
            if (bin!=null) {
                bin.close();
            }
            if (out!=null) {
                out.close();
            }
        }
    }

    /**
     * 这返回的是二进制流，前端只能用blob的方式打开,但是chrome内核的浏览器不一定支持直接打开
     *
     * @param inputStream
     * @param fileName
     * @return
     */
    public static Object downloadFile(InputStream inputStream, String fileName) {
        return ResponseEntity
                .ok()
                .headers(initDownloadHeader(fileName))
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(inputStream));
    }

    /**
     * 采用Content-Disposition：inline
     * 和设置对应返回的contentType，能让前端直接使用window.open()的方式在线打开或下载
     * @param inputStream
     * @param fileName
     * @return
     */
    public static Object downloadFileForInline(InputStream inputStream, String fileName) {
        // 准备header
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");

            // 如果文件有中文，那么就需要转换编码然后传递，前端再转换回对应中文
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll ("\\+", "%20");
            headers.add("Content-Disposition", String.format("inline;filename=\"%s\"", fileName));
            headers.add("File-Name", fileName);
            // 需要暴露这个File-Name
            headers.add("Access-Control-Expose-Headers", "file-name");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception ex){
        }

        // 判断contentType
        // getContentType()
        String mimeType = getMimeType(fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(inputStream));
    }

    public static String getMimeType(String fileName) {
        if (fileName == null) {
            return null;
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "application/octet-stream"; // 没有扩展名，默认为二进制流
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(extension, "application/octet-stream"); // 如果没有匹配的扩展名，则默认为二进制流
    }

    public static void downloadFile(String fileName, ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response){
        initDownloadHeader(response, fileName);
        try{
            // 将 ByteArrayOutputStream 中的数据写入到 HttpServletResponse 的 OutputStream 中
            ServletOutputStream outputStream = response.getOutputStream();
            byteArrayOutputStream.writeTo(outputStream);

            // 关闭流
            byteArrayOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
        }
    }


    /**
     * 下载文件接口 通过OutputStream流
     * @param response
     * @param inputStream
     * @param fileName
     */
    @Deprecated
    public static void downloadFile(HttpServletResponse response, FileInputStream inputStream, String fileName){
        //获取要下载的文件的绝对路径
        OutputStream out = null;
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        try {
            int len = 0;
            //创建数据缓冲区
            byte[] buffer = new byte[1024];
            //通过response对象获取outputStream流
            out = response.getOutputStream();
            //将FileInputStream流写入到buffer缓冲区
            while((len = inputStream.read(buffer)) > 0) {
                //使用OutputStream将缓冲区的数据输出到浏览器
                out.write(buffer,0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化下载请求的header
     * @param downloadFileName
     * @return
     */
    public static HttpHeaders initDownloadHeader(String downloadFileName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");

            // 如果文件有中文，那么就需要转换编码然后传递，前端再转换回对应中文
            downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll ("\\+", "%20");
            headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", downloadFileName));
            headers.add("File-Name", downloadFileName);
            // 需要暴露这个File-Name
            headers.add("Access-Control-Expose-Headers", "file-name");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception ex){
        }
        return headers;
    }

    /**
     * 初始化下载请求的header
     * @param response
     * @param downloadFileName
     */
    public static void initDownloadHeader(HttpServletResponse response, String downloadFileName) {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            // 如果文件有中文，那么就需要转换编码然后传递，前端再转换回对应中文
            downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll ("\\+", "%20");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", downloadFileName));
            response.setHeader("File-Name", downloadFileName);
            // 需要暴露这个File-Name
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        } catch (Exception ex){
        }

    }


    /**
     * 将MultipartFile转化为File类的方法
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static File MultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File toFile = null;
        if (!multipartFile.equals("") && !(multipartFile.getSize() <= 0)) {
            InputStream ins = multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 输入InputStream流，到对应的File文件中
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     * @param filePath
     * @return
     */
    public static String readFileContents(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(new File(filePath));
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

}
