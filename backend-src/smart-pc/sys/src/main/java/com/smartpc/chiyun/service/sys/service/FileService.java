package com.smartpc.chiyun.service.sys.service;

import cn.hutool.core.codec.Base64;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.utils.FileUtil;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    @Autowired
    ServerConfig serverConfig;
    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_ATTACH = "attach";

    public String getFileName(MultipartFile multipartFile) {
        File originalFile = new File(multipartFile.getOriginalFilename());
     //   String suffix = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        return originalFile.getName();
    }

    public SR validateFile(MultipartFile multipartFile, String filetype) {
        SR sr = new SR();
        sr.setStatus(SR.FAIL);
        sr.setMsg("验证失败");
        try {
            Tika tika = new Tika();
            File originalFile = new File(multipartFile.getOriginalFilename());
            String oriName = originalFile.getName();
            String contentType = tika.detect(multipartFile.getInputStream(), oriName);
            long size = multipartFile.getSize();

            if (filetype.equals(FILE_TYPE_IMAGE)) {
                if (!"image/jpeg".equals(contentType)
                        && !"image/gif".equals(contentType)
                        && !"image/png".equals(contentType)) {
                    sr.setMsg("图片格式不正确");
                    return sr;
                }
                String imageMaxSizeKB = serverConfig.getImageMaxSizeKB();
                if (!StringUtils.isEmpty(imageMaxSizeKB)) {
                    long imageMaxSize = Integer.parseInt(imageMaxSizeKB) * 1024;
                    if (size > imageMaxSize) {
                        sr.setMsg("图片过大，请不要超过" + imageMaxSizeKB + "kb");
                        return sr;
                    }
                }

                sr.setStatus(SR.SUCCESS);
                return sr;
            } else if (filetype.equals(FILE_TYPE_ATTACH)) {
                List<String> list = Arrays.asList("application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/zip", "application/pdf",
                        "image/jpeg",
                        "image/gif",
                        "image/png");
                if (!list.contains(contentType)) {
                    sr.setMsg("附件格式不正确");
                    return sr;
                }
                String attachMaxSizeKB = serverConfig.getAttachMaxSizeKB();
                if (!StringUtils.isEmpty(attachMaxSizeKB)) {
                    long attachMaxSize = Integer.parseInt(attachMaxSizeKB) * 1024;
                    if (size > attachMaxSize) {
                        sr.setMsg("附件过大，请不要超过" + attachMaxSize + "kb");
                        return sr;
                    }
                }

                sr.setStatus(SR.SUCCESS);
                return sr;
            }


        } catch (Exception e) {
            log.error("validate file exception", e);
            return sr;
        }
        return sr;

    }

    public SR<File> getFile(String tableName, String id, String fileName) {
        SR<File> sr = new SR<File>();
        sr.setStatus(SR.FAIL);
        sr.setMsg("文件获取失败");
        try {
            String fileSavePath = serverConfig.getFileSavePath();
            if (StringUtils.isEmpty(fileSavePath)) {
                sr.setMsg("保存路径未配置");
                return sr;
            }
            fileSavePath = fileSavePath + File.separator + tableName + File.separator + id;
            File file = new File(fileSavePath, fileName);
            if(file.exists()){
                sr.setMsg(SR.SUCCESS);
                sr.setStatus(SR.SUCCESS);
                sr.setEntity(file);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return sr;
    }

    public SR uploadFile(MultipartFile multipartFile, String tableName) {
        SR sr = new SR();
        sr.setStatus(SR.FAIL);
        sr.setMsg("上传失败");
        if (!StringUtil.isNotNullAndEmpty(tableName)) {
            tableName = "image";
        }
        try {
            String fileSavePath = serverConfig.getFileSavePath();
            if (StringUtils.isEmpty(fileSavePath)) {
                sr.setMsg("保存路径未配置");
                return sr;
            }
            fileSavePath = fileSavePath + File.separator + tableName;
            String fileName = this.getFileName(multipartFile);
            File file = new File(fileSavePath, fileName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            sr.setEntity(fileName);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


    /**
     * 添加文件完整路径中的网络路径
     *
     * @param filePath
     * @return
     * @author 李强
     * @date 2020年5月28日16:04:43
     */
    public String addNetPath(String filePath) {

        if (StringUtil.isNotNullAndEmpty(filePath)
                && filePath.indexOf("http://") == -1 && filePath.indexOf("https://") == -1
                && StringUtil.isNotNullAndEmpty(serverConfig.getFileLoadPath())) {

            return serverConfig.getFileLoadPath() + filePath;
        }
        return filePath;
    }

    public String getNetPath() {
        return serverConfig.getFileLoadPath();
    }


    public String saveImg(String baseImg) throws Exception {
        //定义一个正则表达式的筛选规则，为了获取图片的类型
        String rgex = "data:image/(.*?);base64";
        String type = FileUtil.getSubUtilSimple(baseImg, rgex);
        //去除base64图片的前缀
        baseImg = baseImg.replaceFirst("data:(.+?);base64,", "");
        byte[] b;
        byte[] bs;
        OutputStream os = null;
        String fileName = "";
        String nowDate = "";
        // 格式化并获取当前日期（用来命名）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        nowDate = format.format(new Date());
        //把图片转换成二进制
        b = Base64.decode(baseImg.replaceAll(" ", "+"));
        //生成路径
        String pathUrl = "invoice" + File.separator + "qrcode" + File.separator + nowDate + File.separator;
        String path = serverConfig.getFileSavePath() + File.separator + pathUrl;
        //随机生成图片的名字，同时根据类型结尾
        fileName = UUID.randomUUID().toString() + "." + type;
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        File imageFile = new File(path + "/" + fileName);
        BASE64Decoder d = new BASE64Decoder();
        // 保存
        try {
            bs = d.decodeBuffer(Base64.encode(b));
            os = new FileOutputStream(imageFile);
            os.write(bs);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.info(e.getMessage());
            throw new DataNotFoundException("二维码保存失败");
            // e.printStackTrace();

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
        }
        return pathUrl + fileName;
    }

}
