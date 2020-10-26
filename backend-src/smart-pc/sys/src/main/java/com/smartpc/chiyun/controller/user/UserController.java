package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.WxUtils;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.dao.user.UserGroupDao;
import com.smartpc.chiyun.encryption.Encryption;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.VTUserDX;
import com.smartpc.chiyun.model.sys.vo.QywxVO;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.UserGroup;
import com.smartpc.chiyun.service.user.UserGroupService;
import com.smartpc.chiyun.service.user.UserService;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    UserDao d;
    @Autowired
    UserService s;
    @Autowired
    UserGroupService userGroupService;

    @Autowired
    UserGroupDao userGroupDao;

    @Autowired
    OrgDao orgDao;

    @Log("删除用户")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public SR<User> delete(@PathVariable Long id) {
        SR sr = new SR();
        s.delete(id);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("删除用户")
    @RequestMapping(value = "/user/list", method = RequestMethod.DELETE)
    public SR deletes(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        s.deletes(list);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @ApiOperation("查询用户对象接口")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResultVO load(@PathVariable Long id) {
        User entity = d.findByUserId(id);
        try {
            entity.setGroups(userGroupService.userGroupList(entity.getId()));
            entity.setOrg(orgDao.findOrgById(entity.getOrgId()));
            entity.setOrgName(entity.getOrg().getOrgName());
            return ResultVOUtils.success(entity);
        }catch (Exception e){
            log.error("查询用户对象失败，id:"+id,e);
        }
        return ResultVOUtils.error(ResultEnum.FAILED.getStatus(),"该用户不存在");

    }

    @ApiOperation("查询用户列表接口")
    @Log("查询用户列表")
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public SR<Page<User>> list(@RequestBody BaseQuery<User> query) {
        SR<Page<User>> sr = new SR<>();
        Page<User> pageableList = s.list(query);
      /*  List<User> content = pageableList.getContent();
        for (User u : content) {
            u.setGroups(userGroupService.userGroupList(u.getId()));
            Long orgId = u.getOrgId();
            if (orgId != null) {
                orgDao.findById(orgId).ifPresent(new Consumer<Org>() {
                    @Override
                    public void accept(Org org) {
                        u.setOrg(org);
                    }
                });
            }
            if (User.ADMIN.equals(u.getLevel())) {
                u.setLevelName("超级管理员");
            } else if (User.ORG.equals(u.getLevel())) {
                u.setLevelName("组织管理员");
            } else if (User.GENERAL.equals(u.getLevel())) {
                u.setLevelName("普通用户");
            }
        }*/
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新用户")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody User entity) {
        User user = new User();
        try {
            user = s.updateUser(entity);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error("fail", ex.getMsg());
        }
        return ResultVOUtils.success(user);
    }

    /**
     * 管理员更新密码
     *
     * @param entity
     * @return
     */
    @Log("更新用户密码")
    @PostMapping("/user/updatePwdForManager")
    public SR<User> updatePwdForManager(@RequestBody User entity) {
        SR<User> sr = new SR<>();
        User query = d.findById(entity.getId()).get();
        String newPassword = entity.getNewPassword();
        if (!StringUtils.isEmpty(newPassword.trim())) {
            String newPwdMD5 = Encryption.getEncryptionPassword(newPassword);
            query.setPassword(newPwdMD5);
        }
        User save = d.save(query);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新用户密码")
    @RequestMapping(value = "/user/updatePwd", method = RequestMethod.POST)
    public SR<User> updatePwd(@RequestBody User entity) {
        SR<User> sr = new SR<>();
        User query = d.findById(entity.getId()).get();
        if (query == null) {
            sr.setMsg("用户不存在");
            return sr;
        }
        String username = entity.getUsername();
        if (StringUtils.isEmpty(username)) {
            sr.setMsg("用户名不能为空");
            return sr;
        }
        String password = entity.getPassword();
        if (StringUtils.isEmpty(password)) {
            sr.setMsg("密码不能为空");
            return sr;
        }
        String pwdMD5 = Encryption.getEncryptionPassword(password);
        if (!pwdMD5.equals(query.getPassword()) && !password.equals(query.getPassword())) {
            sr.setMsg("原密码不一致");
            return sr;
        }
        String newPassword = entity.getNewPassword();
        if (StringUtils.isEmpty(newPassword)) {
            sr.setMsg("新密码不能为空");
            return sr;
        }
        String newPwdMD5 = Encryption.getEncryptionPassword(newPassword);
        query.setUsername(username);
        query.setPassword(newPwdMD5);
        User save = d.save(query);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("新增用户")
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public SR<User> add(@RequestBody User entity) {
        Long orgId = entity.getOrgId();
        SR<User> sr = new SR<>();
        String username = entity.getUsername();
        if (StringUtils.isEmpty(username)) {
            sr.setMsg("用户名不能为空");
            return sr;
        }
        List<User> list = s.findByUsername(username);
        if (list.size() > 0) {
            sr.setMsg("用户名不能重复");
            return sr;
        }
        String realName = entity.getRealName();
        if (StringUtils.isEmpty(realName)) {
            sr.setMsg("用户真实姓名不能为空");
            return sr;
        }
        entity.setPassword(entity.getUsername());//TO SPECIAL:密码暂时在创建时和用户名一致
        String password = Encryption.getEncryptionPassword(entity.getPassword());
        entity.setPassword(password);
        User user = d.save(entity);
        user.setOrgId(orgId);
        //自动为用户生成角色信息
        UserGroup userGroup = new UserGroup();
        userGroup.setOrgId(orgId);
        userGroup.setUserId(user.getId());
        //数据权限  根据用户等级来分配所属角色
        if (User.ADMIN.equals(user.getLevel()) ) { //超级管理员查看全部
            userGroup.setGroupId(1L);
        }else if ( User.ORG.equals(user.getLevel())){//省市管理员看对应的省市
            userGroup.setGroupId(2L);
        }else{//普通用户
            userGroup.setGroupId(3L);
        }
        userGroupDao.save(userGroup);
        User save = d.saveAndFlush(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/user/listByOrg", method = RequestMethod.GET)
    public ResultVO listByOrg() {
        List<User> users = d.findUsersByOrgId(AppUtil.getCurrentUserOrgId());
        ResultVO vo = ResultVOUtils.success(users);
        vo.setMsg(AppUtil.getCurrentUser().getId() + "");
        return vo;
    }

    /**
     * 用户界面同步企业微信提交
     *
     * @param qywxVO
     * @return
     */
    @Log("同步企业微信用户")
    @RequestMapping(value = "/user/syncQywxUser", method = RequestMethod.POST)
    public ResultVO syncQywxUser(@RequestBody QywxVO qywxVO) {

        String corpid = qywxVO.getCorpid();
        String corpsecret = qywxVO.getCorpsecret();
        Long deptId = qywxVO.getDeptId();
        //获取token
        String accessToken = "";
        try {
            accessToken = WxUtils.getAccessToken(corpid, corpsecret);
        } catch (Exception e) {
            return ResultVOUtils.error(ResultEnum.ACCESS_TOKEN_ERROR);
        }
        log.info("qywx access_token = {}", accessToken);
        if (StringUtil.isNotEmpty(accessToken)) {

            //获取用户列表
            List<VTUserDX> deptUsers = WxUtils.getDeptUsers(accessToken, deptId);
            //qywx users => local users
            s.saveQywxUsers(deptUsers);
            s.saveQywxUsersToMappTable(deptUsers);
        } else {
            ResultVOUtils.error(ResultEnum.ACCESS_TOKEN_ERROR);
        }

        return ResultVOUtils.success();
    }

    @GetMapping("/user/findUserById/{id}")
    public ResultVO findUserById(@PathVariable Long id) {
        User user = s.findUserById(id);
        return ResultVOUtils.success(user);
    }


}
