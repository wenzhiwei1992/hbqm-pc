package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.controller.user.dto.CompanyVO;
import com.smartpc.chiyun.dao.user.CompanyDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.service.sys.service.FileService;
import com.smartpc.chiyun.service.user.CompanyService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.user.Company;


import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class CompanyController {
    @Autowired
    CompanyDao companyDao;
    @Autowired
    CompanyService companyService;
    @Autowired
    FileService fileService;
    @Autowired
    ServerConfig serverConfig;
    @GetMapping("/company/getCompanyById/{id}")
    public SR<Company> getCompanyById(@PathVariable Long id){
        SR<Company> sr = new SR<>();
        Company company   = null;
        if(id!=null&& ! "".equals(id)){
            company =   companyService.getCompanyById(id);
            sr.setStatus(SR.SUCCESS);
        }else{
            sr.setStatus(SR.FAIL);
        }
        sr.setEntity(company);
        return sr;
    }
    @RequestMapping(value = "/company/list", method = RequestMethod.POST)
    public ResultVO list(@RequestBody CompanyVO companyVO) {
        if (companyVO.getPageInfo().getPageSize() <= 0 || companyVO.getPageInfo().getCurrentPage() <= 0) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ResultEnum.PAGEINFODATAERR.getMsg(), "");
        }
        Page<Company> list = companyService.list(companyVO);

        return ResultVOUtils.success(list);
    }
    @PostMapping("/company/add")
    public SR<Company>  add(@RequestBody Company company){
        SR<Company> sr = new SR<>();
        companyDao.save(company);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @PostMapping("/company/update")
    public SR<Company> update(@RequestBody Company company){
        SR<Company> sr = new SR<>();
        if(company!=null){
            Company companyUpdate =  companyService.getCompanyById(company.getId());
            companyUpdate.setCompanyName(company.getCompanyName());
            companyUpdate.setFactoryIdCode(company.getFactoryIdCode());
            companyUpdate.setLogoUrl(company.getLogoUrl());
            companyDao.save(companyUpdate);
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
    public SR<Company> delete(@PathVariable Long id) {
        SR sr=new SR();
        try{
            companyService.deleteCompanyById(id);
        }catch (Exception e){
            sr.setMsg("该信息已有关联信息,不能删除！");
            return  sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @RequestMapping(value = "/company/companys", method = RequestMethod.DELETE)
    public SR<Company> delete(String ids) {
        String[] split = ids.split(";");
        List<Long> list=new ArrayList<>();
        for(String id:split){
            list.add(Long.parseLong(id));
        }
        SR sr=new SR();
        try{
            companyService.deleteCompanysWithIds(list);
        }catch (Exception e){
            sr.setMsg("已选信息中已有其他关联信息,不能删除！");
            return  sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @PostMapping(value = "/company/getUploadPhoto")
    public SR<String> getUploadPhoto(@RequestParam(value = "file") MultipartFile file) {
        SR<String> sr = new SR<>();
        Calendar calendar = Calendar.getInstance();
        //创建保存在服务器上存放图片的文件夹（根据时间）
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String id = year+month+date+UUID.randomUUID();
        String fileSavePath = "";
        String fileName="";
        SR validateSR=fileService.validateFile(file, FileService.FILE_TYPE_IMAGE);
        if(validateSR.getStatus().equals(SR.FAIL)){
            sr.setMsg(validateSR.getMsg());
            return sr;
        }
      //  fileService.uploadFile(file,FileService.FILE_TYPE_IMAGE, Check.TABLE_NAME,save.getId()+"");
        try{
             fileSavePath = serverConfig.getFileSavePath();
            if(StringUtils.isEmpty(fileSavePath)){
                sr.setMsg("保存路径未配置");
                return sr;
            }
            fileSavePath = fileSavePath + File.separator + Company.TABLE_NAME + File.separator + id;
            fileName=fileService.getFileName(file);
            File file1 = new File(fileSavePath, fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(),file1);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
            return sr;
        }
        sr.setEntity(fileSavePath+File.separator+fileName);
        sr.setStatus(SR.SUCCESS);
        return  sr;
    }
}
