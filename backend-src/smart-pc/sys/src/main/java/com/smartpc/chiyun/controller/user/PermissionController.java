package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.security.service.PermissionService;
import com.smartpc.chiyun.dao.user.GroupPermissionDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.model.sys.vo.GroupPermissionVO;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.user.GroupPermission;
import com.smartpc.chiyun.model.user.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PermissionController {

    @Autowired
    GroupPermissionDao groupPermissionDao;

    @Autowired
    PermissionService permissionService;

    /**
     * 根据groupId获取所有的permissionId
     * @param groupId
     * @return
     */
    @GetMapping("/permission/findById/{groupId}")
    public ResultVO findByGroupId(@PathVariable Long groupId){
        List<GroupPermission> allByGroupId = groupPermissionDao.findAllByGroupId(groupId);
        List<Long> longs = allByGroupId.stream().map(GroupPermission::getPermissionId).collect(Collectors.toList());
        return ResultVOUtils.success(longs);
    }

    /**
     * 加载权限tree
     * @return
     */
    @GetMapping("/permission/loadTree")
    public ResultVO loadPermissionTree(){
        List<Permission> list = permissionService.loadTree();
        return ResultVOUtils.success(list);
    }

    /**
     * 更新权限tree
     * @return
     */
    @Log("更新权限Tree")
    @PostMapping("/permission/update")
    @Transactional(rollbackForClassName = "Exception")
    public ResultVO update(@RequestBody GroupPermissionVO gvo){
        Long groupId = gvo.getGroupId();
        if (StringUtils.isEmpty(groupId)){
            return ResultVOUtils.error(ResultEnum.DATASIZEISZORE);
        }
        //1.删除之前所有的权限
        groupPermissionDao.deleteByGroupId(groupId);
        //2.添加新的权限
        List<Long> permissionIds = gvo.getPermissionIds();
        List<GroupPermission> list = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            GroupPermission groupPermission = new GroupPermission();
            groupPermission.setGroupId(groupId);
            groupPermission.setPermissionId(permissionId);
            list.add(groupPermission);
        }
        groupPermissionDao.saveAll(list);
        return ResultVOUtils.success();
    }

    /**
     * 根据ID获取权限信息
     * @param id
     * @return
     */
    @GetMapping("/permission/findByPmId/{id}")
    public ResultVO findByPmId(@PathVariable Long id){
        Permission permission = permissionService.loadById(id);
        if (permission==null){
            return ResultVOUtils.error(ResultEnum.DATASIZEISZORE);
        }
        return ResultVOUtils.success(permission);
    }

    /**
     * 根据ID获取权限信息
     * @return
     */
    @GetMapping("/permission/loadPids")
    public ResultVO loadPids(){
        List<Permission> permissions = permissionService.loadPids();
        return ResultVOUtils.success(permissions);
    }
    /**
     * 新增权限信息
     * @return
     */
    @Log("新增权限")
    @PostMapping("/permission/add")
    public ResultVO add(@Valid @RequestBody Permission permission){
        Permission permission0 = permissionService.add(permission);
        if (permission0 == null) {
            return ResultVOUtils.error(ResultEnum.FAILED);
        }
        return ResultVOUtils.success();
    }

    @Log("删除权限")
    @DeleteMapping("/permission/delete/{id}")
    public ResultVO add(@PathVariable Long id){
        permissionService.deleteById(id);
        return ResultVOUtils.success();
    }


}
