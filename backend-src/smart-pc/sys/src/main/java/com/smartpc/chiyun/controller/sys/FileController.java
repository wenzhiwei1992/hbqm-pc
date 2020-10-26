package com.smartpc.chiyun.controller.sys;

import com.alibaba.fastjson.JSON;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.ServerConfig;
import com.smartpc.chiyun.service.sys.service.FileService;
import com.smartpc.chiyun.utils.FileUtil;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = "文件上传相关接口")
@RestController
@CrossOrigin
@Slf4j
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    ServerConfig serverConfig;

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void licenseImg(HttpServletResponse response, String tableName, String id, String fileName) {
        SR<File> fileResult = fileService.getFile(tableName, id, fileName);
        if (fileResult.getStatus().equals(SR.FAIL)) {
            return;
        }
        File file = fileResult.getEntity();
        try {
            BufferedImage im = ImageIO.read(file);
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            ImageIO.write(im, formatName, response.getOutputStream());
        } catch (Exception e) {
            log.error("image read error", e);
        }

    }


    /**
     * 下载模板文件
     * @return
     */
    @GetMapping(value = "/file/exportTemplateFile")
    public SR<Object> export(HttpServletResponse response,String fileName) throws Exception {
        SR<Object> sr = new SR<Object>();
        SR<File> file = fileService.getFile("", "", fileName);
        if("fail".equals(file.getStatus())){
            return sr;
        }
        FileUtil.fileResponse(response, fileName);

        byte[] buff = new byte[1024];

        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file.getEntity()));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /**
     * 上传模板文件
     * @param file
     * @return
     */
    @PostMapping(value = "/file/uploadTemplateFile")
    public SR<String> uploadTemplateFile(@RequestParam(value = "file") MultipartFile file) {
        SR<String> sr = new SR<>();
        String fileSavePath = serverConfig.getFileSavePath();
        if (StringUtils.isEmpty(fileSavePath)) {
            sr.setMsg("保存路径未配置");
            return sr;
        }
        try {
            String fileName = fileService.getFileName(file);
            File file2 = new File(fileSavePath, fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), file2);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


    /**
     * 判断模板文件是否存在
     * @param fileName
     * @return
     */
    @GetMapping(value = "file/exportFileExists")
    public ResultVO exportExcelSfg1(String fileName) {
        SR<File> file = fileService.getFile("", "", fileName);
        if ("fail".equals(file.getStatus())) {
            return ResultVOUtils.error(ResultEnum.READ_EXCEL_ERROR.getStatus(), "读取文件失败,请检查文件"+fileName+"是否存在");
        }
        return ResultVOUtils.success();
    }



    @RequestMapping(value = "/fileUpload/upload", method = RequestMethod.POST)
    public SR<String> upload(MultipartFile[] fileupload, String folderName) {
        SR<String> sr = new SR<>();
        String checkImages = "";
        if (fileupload != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            for (MultipartFile file : fileupload) {
                SR validateSR = fileService.validateFile(file, FileService.FILE_TYPE_IMAGE);
                if (validateSR.getStatus().equals(SR.FAIL)) {
                    sr.setMsg(validateSR.getMsg());
                    return sr;
                }
                checkImages += checkImages.equals("") ? fileService.getFileName(file) : ";" + fileService.getFileName(file);
            }
            for (MultipartFile file : fileupload) {
                SR info = fileService.uploadFile(file, folderName);
                log.info("uploadInfo:--------------------------" + JSON.toJSONString(info));
            }
        }
        sr.setEntity(checkImages);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


    /**
     * 上传图片接口  该方法返回结果为相对路径， folderName+时间+文件名
     * 李强
     *
     * @param fileupload
     * @param folderName
     * @return
     */

    @ApiOperation("上传图片接口")
    @RequestMapping(value = "/fileUpload/uploadFile", method = RequestMethod.POST)
    public SR<String> uploadFile(MultipartFile[] fileupload, String folderName) {
        SR<String> sr = new SR<>();
        String images = "";
        String imagePaths = "";
        if (fileupload != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            for (MultipartFile file : fileupload) {
                SR validateSR = fileService.validateFile(file, FileService.FILE_TYPE_IMAGE);
                if (validateSR.getStatus().equals(SR.FAIL)) {
                    sr.setMsg(validateSR.getMsg());
                    return sr;
                }
                images += images.equals("") ? fileService.getFileName(file) : ";" + fileService.getFileName(file);
                imagePaths += folderName + File.separator + dateStr + File.separator + fileService.getFileName(file) + ";";
            }
            for (MultipartFile file : fileupload) {
                SR info = fileService.uploadFile(file, folderName + File.separator + dateStr);
                log.info("uploadInfo:--------------------------" + JSON.toJSONString(info));
            }
        }
        if (StringUtil.isNotNullAndEmpty(imagePaths)) {
            imagePaths = imagePaths.substring(0, imagePaths.length() - 1);
            images = images.substring(0, images.length() - 1);
        }
        sr.setEntity(imagePaths);
        sr.setMsg(images);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
}
