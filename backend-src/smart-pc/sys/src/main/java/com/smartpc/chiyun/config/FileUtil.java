package com.smartpc.chiyun.config;


import com.smartpc.chiyun.service.sys.service.FileService;
import com.smartpc.chiyun.model.sys.ServerConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件工具类
 */
@Component
public class FileUtil {

    @Autowired
    ServerConfig serverConfig;

    @Autowired
    FileService fileService;

    public File formatPlanBarPXML(MultipartFile multipartFile) throws Exception {

        String fileName = fileService.getFileName(multipartFile);

        String path = normalize(serverConfig.getFileSavePath());
        // 读
        File file = new File(path + "/pxml/" + fileName);

        FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());

        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);

        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
        // 替换
        String line = null;

        //跳过第一行 ，因为是 xml 头
        String line1 = bufIn.readLine();

        // 将该行写入内存
        tempStream.write(line1);
        // 添加换行符
        tempStream.append(System.getProperty("line.separator"));

        //第二行
        String line2 = bufIn.readLine();
        if (line2.contains("<ROOT>")) {
            //忽略，不做任何操作
        } else {
            line2 = "<ROOT>" + System.getProperty("line.separator") + line2;
        }
        // 将该行写入内存
        tempStream.write(line2);
        // 添加换行符
        tempStream.append(System.getProperty("line.separator"));

        while ((line = bufIn.readLine()) != null) {

            // 替换每行中, 符合条件的字符串
            if (line.contains("/PXML_Document")) {
                line = "</PXML_Document>";
            }
            //去除已经处理过的文件最后一行
            if (line.contains("/ROOT")) {
                line = "";
            }
            //System.out.println("line after: "+line);
            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
        }

        // 关闭 输入流
        bufIn.close();

        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.append("</ROOT>");
        out.close();
        return file;
    }


    /* Check that the given pathname is normal.  If not, invoke the real
       normalizer on the part of the pathname that requires normalization.
       This way we iterate through the whole pathname string only once. */
    public String normalize(String pathname) {
        int n = pathname.length();
        char prevChar = 0;
        for (int i = 0; i < n; i++) {
            char c = pathname.charAt(i);
            if ((prevChar == '/') && (c == '/'))
                return normalize(pathname, n, i - 1);
            prevChar = c;
        }
        if (prevChar == '/') return normalize(pathname, n, n - 1);
        return pathname;
    }


    /* A normal Unix pathname contains no duplicate slashes and does not end
       with a slash.  It may be the empty string. */

    /* Normalize the given pathname, whose length is len, starting at the given
       offset; everything before this offset is already normal. */
    private String normalize(String pathname, int len, int off) {
        if (len == 0) return pathname;
        int n = len;
        while ((n > 0) && (pathname.charAt(n - 1) == '/')) n--;
        if (n == 0) return "/";
        StringBuffer sb = new StringBuffer(pathname.length());
        if (off > 0) sb.append(pathname.substring(0, off));
        char prevChar = 0;
        for (int i = off; i < n; i++) {
            char c = pathname.charAt(i);
            if ((prevChar == '/') && (c == '/')) continue;
            sb.append(c);
            prevChar = c;
        }
        return sb.toString();
    }


    // 文件复制
    public static boolean copyFile(String source, String copy) throws Exception {
        source = source.replace("\\", "/");
        copy = copy.replace("\\", "/");

        File source_file = new File(source);
        File copy_file = new File(copy);

        // BufferedStream缓冲字节流

        if (!source_file.exists()) {
            throw new IOException("文件复制失败：源文件（" + source_file + "） 不存在");
        }
        if (copy_file.isDirectory()) {
            throw new IOException("文件复制失败：复制路径（" + copy_file + "） 错误");
        }
        File parent = copy_file.getParentFile();
        // 创建复制路径
        if (!parent.exists()) {
            parent.mkdirs();
        }
        // 如果复制文件存在就先删除
        if (copy_file.exists()) {
            copy_file.delete();
        }
        // 创建复制文件
        if (!copy_file.exists()) {
            copy_file.createNewFile();
        }

        FileInputStream fis = new FileInputStream(source_file);
        FileOutputStream fos = new FileOutputStream(copy_file);

        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] KB = new byte[1024];
        int index;
        while ((index = bis.read(KB)) != -1) {
            bos.write(KB, 0, index);
        }

        bos.close();
        bis.close();
        fos.close();
        fis.close();

        if (!copy_file.exists()) {
            return false;
        } else if (source_file.length() != copy_file.length()) {
            return false;
        } else {
            return true;
        }

    }

    // 文件重命名
    public static boolean renameFile(String url, String new_name) throws Exception {
        String old_url = url;
        old_url = old_url.replace("\\", "/");
        File old_file = new File(old_url);
        if (!old_file.exists()) {
            throw new IOException("文件重命名失败，文件（" + old_file + "）不存在");
        }
        System.out.println(old_file.exists());

        String old_name = old_file.getName();
        // 获得父路径
        String parent = old_file.getParent();
        // 重命名
        String new_url = parent + "/" + new_name;
        File new_file = new File(new_url);
        old_file.renameTo(new_file);

        System.out.println("原文件：" + old_file.getName());
        System.out.println("新文件：" + new_file.getName());
        new_name = new_file.getName();
        old_name = old_file.getName();
        if (new_name.equals(old_name)) {
            return false;
        } else {
            return true;
        }

    }

    // 文件删除
    public static boolean deleteFile(String url) throws Exception {
        url = url.replace("\\", "/");
        File file = new File(url);

        if (file.isFile()) {
            if (file.exists()) {
                file.delete();
            }
        } else {
            throw new IOException("文件删除失败：（" + file + "）错误");
        }
        if (file.exists()) {
            return false;
        } else {
            return true;
        }
    }

    // 创建文件夹
    public static boolean createPath(String url) throws Exception {
        url = url.replace("\\", "/");
        File folder = new File(url);
        if (!folder.isDirectory()) {
            throw new IOException("创建文件夹失败：（" + folder + "）不是文件夹路径");
        }

        if (!folder.isFile()) {
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        // 检测是否创建成功
        if (folder.isDirectory() && folder.exists()) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 获取文件夹下的全部文件名称
     *
     * @param path         文件夹路径全称
     * @param fileNameList 返回文件名集合
     */
    public static void getAllFileName(String path, ArrayList<String> fileNameList) {
        boolean flag = false;
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
//              ///System.out.println("文     件：" + tempList[i]);
                fileNameList.add(tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
//              ///System.out.println("文件夹：" + tempList[i]);
                getAllFileName(tempList[i].getAbsolutePath(), fileNameList);
            }
        }
        return;
    }




}
