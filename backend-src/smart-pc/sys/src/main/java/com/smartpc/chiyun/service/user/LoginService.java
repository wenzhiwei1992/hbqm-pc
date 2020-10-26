
package com.smartpc.chiyun.service.user;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.db.TenantContext;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.domain.Log;
import com.smartpc.chiyun.model.user.Org;
import com.smartpc.chiyun.service.LogService;
import com.smartpc.chiyun.utils.*;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.ServerConfig;
import com.smartpc.chiyun.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserGroupService userGroupService;

    @Qualifier("PCUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    ServerConfig serverConfig;

    @Autowired
    AppUtil appUtil;

    @Autowired
    OrgDao orgDao;

    @Autowired
    LogService logService;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    CodeExplainContentDao codeExplainContentDao;


    public SR<User> setLoginUser(User user) {
        Long currentTime = System.currentTimeMillis();

        SR<User> sr = new SR<User>();
        sr.setStatus(SR.FAIL);

        if (user == null) {
            sr.setMsg("该用户不存在");
            return sr;
        }


        //数据权限  根据用户等级来分配所能查看的组织信息
        if (User.ADMIN.equals(user.getLevel()) ) { //超级管理员查看全部
            List<Org> orgs = orgDao.findAll();
            List<Long> orgsIds = orgs.stream().map(Org::getId).collect(Collectors.toList());
            user.setOrgIdsData(orgsIds);
        }else if ( User.ORG.equals(user.getLevel())){//省市管理员看对应的省市
            List<Org> orgs = orgDao.findOrgsByProvinceIdOrCityId(user.getDeptId());
            List<Long> orgsIds = orgs.stream().map(Org::getId).collect(Collectors.toList());
            user.setOrgIdsData(orgsIds);
        }else{//普通用户
            user.setOrgIdsData(Arrays.asList(user.getOrgId()));
        }

        if (user.getDeptId() != null) {
            Optional<Department> optional = departmentDao.findById(user.getDeptId());
            if (optional.isPresent()) {
                user.setDeptName(optional.get().getDeptName());
            }
        }

        user.setGroups(userGroupService.userGroupList(user.getId()));

        User authUser = (User) this.userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                authUser, "", authUser.getAuthorities());
        newAuth.setDetails(authUser);

        log.info("-------------------authUser--------------------");
        log.info("-------------------authUser-------->" + authUser);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        log.info("----------------------- SecurityContextHolder.getContext().setAuthentication(newAuth)-----------------------");
        log.info("-------------------newAuth-------->" + newAuth.getDetails());
        //必须放在被更改的entity前面，否则会被hibernate提交（或者返回对象使用DTO）
        Log log = new Log("INFO", System.currentTimeMillis() - currentTime);
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveManual("用户登录", SecurityUtils.getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), log);
        user.setPassword(null);
        sr.setStatus(SR.SUCCESS);
        sr.setEntity(user);
        if(StringUtil.isNotNullAndEmpty(user.getJSessionId())){
            EhcacheUtil ehcacheUtil =  EhcacheUtil.getInstance();
            ehcacheUtil.put("pdaUser",user.getJSessionId(),newAuth);
        }
        return sr;
    }


    public SR<User> refreshLoginUser() {
        SR<User> sr = new SR<User>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {
            if (serverConfig.getEnvType().equals("dev")) {
                //tryAutoLogin();
            } else {
                log.info("--------------dev---------");
                log.info("-------------" + serverConfig.getEnvType() + "---------");
                log.info("-------------------------------------------");
                sr.setMsg("您尚未登录");
                return sr;
            }
        }
        auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {
            log.info("--------------auth---------");
            log.info("-------------" + auth + "---------");
            log.info("-------------------------------------------");
            sr.setMsg("您尚未登录");
            return sr;
        }
        if (!auth.isAuthenticated()) {
            log.info("--------------auth.isAuthenticated---------");
            log.info("-------------" + auth.isAuthenticated() + "---------");
            log.info("-------------------------------------------");
            sr.setMsg("您尚未登录");
            return sr;
        }
        User user = (User) auth.getDetails();
        String tenantId = user.getTenantId();
        TenantContext.setCurrentTenant(tenantId);
        SecurityContextHolder.getContext().setAuthentication(auth);
        sr.setStatus(SR.SUCCESS);
        sr.setEntity(user);

        return sr;

    }

    public User findByPassword(String password) {

        return userDao.findByPassword(password);

    }

    public JSONObject loadLoginInfo() {
        JSONObject jsonObject = new JSONObject();
        if (!StringUtils.isEmpty(serverConfig.getQywxCorpid()) && !StringUtils.isEmpty(serverConfig.getQywxSecret()) && !StringUtils.isEmpty(serverConfig.getTenantId())) {
            jsonObject.put("corpId", serverConfig.getQywxCorpid());
            jsonObject.put("secret", serverConfig.getQywxSecret());
            jsonObject.put("tenantId", serverConfig.getTenantId());
        }
        return jsonObject;
    }

    public List<String> loadLoginInfodd() {
        List<CodeExplainContent> codes = codeExplainContentDao.findCodeExplainContentByCodeNoAndCodeName("DingDingConfig", "CorpId");
        List<String> corpids = new ArrayList<>();
        for (CodeExplainContent code : codes) {
            corpids.add(code.getRemark());
        }
        return corpids;
    }
}
