package com.smartpc.chiyun.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.encryption.Encryption;
import com.smartpc.chiyun.service.sys.service.FileService;
import com.smartpc.chiyun.service.user.LoginService;
import com.smartpc.chiyun.utils.RequestHolder;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.ServerConfig;
import com.smartpc.chiyun.model.user.User;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 */
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    OrgDao orgDao;
    @Autowired
    FileService fileService;


    /**
     * 不能使用 log 进行登录，只能手动写。
     *
     * @Log("用户登录")
     **/
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public SR<User> login(@RequestBody User user) {
        SR<User> sr = new SR<>();
        String username = user.getUsername();
        User query = userDao.findByUsername(username);
        if (query == null) {
            sr.setMsg("用户不存在");
            return sr;
        }else if (!user.getEnabled()){//用户被停用
            sr.setMsg("该用户已被停用，请联系管理员核对信息");
            return sr;
        }
        String password = user.getPassword();
        String pwdMd5 = Encryption.getEncryptionPassword(password);
        User user1 = userDao.findByPasswordAndUserName(pwdMd5, username);
        User user2 = userDao.findByPasswordAndUserName(password, username);
        if (user1 == null && user2 == null) {
            sr.setMsg("密码不正确");
            return sr;
        }
        query.setTenantId(user.getTenantId());
        try {
            query.setOrg(orgDao.findOrgById(query.getOrgId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (query != null && StringUtil.isNotNullAndEmpty(RequestHolder.getHttpServletRequest().getSession().getId())) {
            query.setJSessionId(RequestHolder.getHttpServletRequest().getSession().getId());
        }
        SR<User> userSr = loginService.setLoginUser(query);


        return userSr;
    }

    @RequestMapping(value = "/user/login/refreshLoginUser", method = RequestMethod.POST)
    public SR<User> refreshLoginUser() {
        SR<User> sr = loginService.refreshLoginUser();
        return sr;
    }

    @RequestMapping(value = "/user/login/orgs", method = RequestMethod.GET)
    public SR<JSONArray> loginOrgs() {
        SR<JSONArray> sr = new SR<>();
        JSONArray orgs = serverConfig.getOrgs();
        sr.setStatus(SR.SUCCESS);
        sr.setEntity(orgs);
        return sr;
    }

    @GetMapping(value = "/user/login/loadLoginInfo")
    public ResultVO loadLoginInfo() {
        JSONObject jsonObject = loginService.loadLoginInfo();
        return ResultVOUtils.success(jsonObject);
    }

    @GetMapping(value = "/user/login/loadLoginInfodd")
    public ResultVO loadLoginInfodd() {
        List<String> codes = loginService.loadLoginInfodd();
        if (codes != null && codes.size() > 0) {
            return ResultVOUtils.success(codes.get(0));
        }
        return ResultVOUtils.success("");
    }
}
