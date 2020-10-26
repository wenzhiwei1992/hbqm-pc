package com.smartpc.chiyun.controller.dingding;


import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.controller.user.dto.SsoDTO;
import com.smartpc.chiyun.dao.syscode.CodeExplainDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.dao.user.UserM2EpWxDao;
import com.smartpc.chiyun.enums.ResultEnum;

import com.smartpc.chiyun.service.DingDingConfigService;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.syscode.CodeExplain;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.UserM2EpWx;
import com.smartpc.chiyun.service.user.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class DingDingController {


    @Autowired
    DingDingConfigService dingDingConfigService;

    @Autowired
    UserDao userDao;

    @Autowired
    OrgDao orgDao;

    @Autowired
    private UserM2EpWxDao userM2EpWxDao;

    @Autowired
    private CodeExplainDao codeExplainDao;


    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/dingding/getAccesstoken", method = RequestMethod.GET)
    public String getAccesstoken() {
        return dingDingConfigService.getAccessToken();
    }


    @RequestMapping(value = "/dingding/getJsapiTicket", method = RequestMethod.GET)
    public String getJsapiTicket() {
        return dingDingConfigService.getJsapiTicket();
    }


    @RequestMapping(value = "/dingding/getUserInfo", method = RequestMethod.GET)
    public String getUserInfo(String code) {
        return dingDingConfigService.getUserInfo(code, AppUtil.getCurrentUserOrgId());
    }


    @PostMapping("/user/m2ep/ssodd")
    public ResultVO ssodd(@RequestBody SsoDTO ssoDTO) {
        String code = ssoDTO.getCode();
        String corpId = ssoDTO.getCorpId();
        String secret = ssoDTO.getSecret();
        String tenantId = ssoDTO.getTenantId();


        if (StringUtils.isEmpty(code)) {
            return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);
        }

        List<CodeExplain> lists = codeExplainDao.findAllByCodeNo("DingDingConfig");
        for (CodeExplain codeExplain : lists) {
            String qywxUserId = dingDingConfigService.getUserInfo(code, codeExplain.getOrgId());
            if (StringUtil.isEmpty(qywxUserId)) {
                continue;
            }
            //处理登录事项
            List<UserM2EpWx> list = userM2EpWxDao.findByEpUserId(qywxUserId, 2);
            if (list.size() > 0) {
                UserM2EpWx epWx = list.get(0);
                if (epWx != null) {
                    User query = null;
                    //这里这样写 是为了兼容可能出现的以前的错误数据
                    //以前Userid存的是用户名  现在改为用户id  数2020年4月28日16:21:08  李强
                    try {
                        Long userId = Long.parseLong(epWx.getUserId() + "");
                        query = userDao.findByUserId(userId);

                    } catch (Exception e) {
                        query = userDao.findByUsername(epWx.getUserId() + "");
                    }
                    query.setTenantId(tenantId);
                    try {
                        query.setOrg(orgDao.findById(query.getOrgId()).get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SR<User> user = loginService.setLoginUser(query);
                    return ResultVOUtils.success(user.getEntity());
                }
            }
        }

        return ResultVOUtils.error(ResultEnum.USER_NOT_FOUND_ERROR);


    }


    @RequestMapping(value = "/user/m2ep/syncQyddUser", method = RequestMethod.GET)
    public ResultVO syncQyddUser() {
        Long orgId = AppUtil.getCurrentUserOrgId();

        String token = dingDingConfigService.getAccessToken(orgId);
        if (!StringUtil.isNotNullAndEmpty(token)) {
            return ResultVOUtils.error("failed", "请联系管理员检查钉钉配置");
        }
        List<User> users = userDao.findUsersByOrgId(orgId);
        for (User user : users) {
            if (StringUtil.isNotNullAndEmpty(user.getMobile())) {
                String qywxUserId = dingDingConfigService.getUserInfoByPhone(user.getMobile());
                if (StringUtil.isNotNullAndEmpty(qywxUserId) && qywxUserId.indexOf("不合法") == -1) {
                    UserM2EpWx m2 = new UserM2EpWx();
                    m2.setEpWxUserId(qywxUserId);
                    m2.setUserId(user.getId() + "");
                    m2.setType(2);
                    m2.setOrgId(AppUtil.getCurrentUserOrgId());
                    m2.setPhone(user.getMobile());
                    List<UserM2EpWx> ep = userM2EpWxDao.findByUserIdAndEpWxUserId(user.getId() + "", qywxUserId, 2);
                    if (ep == null || ep.isEmpty()) {
                        userM2EpWxDao.save(m2);
                    }
                }

            }
        }

        return ResultVOUtils.success();


    }





}
