package com.smartpc.chiyun.controller.user;

import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.service.user.GroupMenuService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.GroupMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author yue
 * @create 2020/3/17 11:07 下午
 */
@RestController
public class GroupMenuController {
    @Autowired
    GroupMenuService groupMenuService;

    @PostMapping("/groupMenu/updateGroupPermission")
    public ResultVO updateGroupPermission(@RequestBody Group resources) {
        try {
            groupMenuService.updateGroupPermission(resources);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success();
    }

    @GetMapping("/groupMenu/findGroupMenusByGroupId/{groupId}")
    public ResultVO findGroupMenusByGroupId(@PathVariable Long groupId) {
        List<GroupMenu> groupMenuList = groupMenuService.findGroupMenusByGroupId(groupId);

        return ResultVOUtils.success(groupMenuList);
    }

    @GetMapping("/groupMenu/findIndexByGroupId/{groupId}")
    public ResultVO findIndexByGroupId(@PathVariable Long groupId) {
        List<String> stringList = null;
        try {
            stringList = groupMenuService.findIndexByGroupId(groupId);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success(stringList);
    }
}
