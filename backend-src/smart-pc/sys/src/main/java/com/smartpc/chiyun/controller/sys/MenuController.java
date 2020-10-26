package com.smartpc.chiyun.controller.sys;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.service.sys.dto.MenuDto;
import com.smartpc.chiyun.service.sys.service.MenuService;
import com.smartpc.chiyun.exception.EntityExistException;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.sys.Menu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "菜单相关接口")
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping("/sys/menu/getMenuList")
    public ResultVO getMenuList() {
        Map<String, Object> menuTree = menuService.buildMenuTree();
        return ResultVOUtils.success(menuTree);
    }

    @ApiOperation("pc端加载用户菜单接口")
    @PostMapping("/sys/menu/getMenuListByUser")
    public ResultVO getMenuListByUser(@RequestBody User user) {
        //用于区分app端和pc端调用
        user.setPortFlag(true);
        Map<String, Object> menuTree = menuService.getMenuByLevel(user);
        return ResultVOUtils.success(menuTree);
    }

    @ApiOperation("app端加载用户菜单接口")
    @GetMapping("/sys/menu/getMenuListByUserApp")
    public ResultVO getMenuListByUserApp() {
        User user = AppUtil.getCurrentUser();
        //用于区分app端和pc端调用
        user.setPortFlag(false);
        Map<String, Object> menuTree = menuService.getMenuByLevel(user);
        return ResultVOUtils.success(menuTree);
    }

    @GetMapping("/sys/menu/getMenuListByAppId/{appId}")
    public ResultVO getMenuListByAppId(@PathVariable Integer appId) {
        Map<String, Object> menuTree = menuService.buildMenuTreeByAppId(appId);
        return ResultVOUtils.success(menuTree);
    }

    @PostMapping("/sys/menu/getMenuListByQuery")
    public ResultVO getMenuListByQuery(@RequestBody MenuDto menuDto) {
        List<Menu> menuList = menuService.getMenuListByQuery(menuDto);
        Map<String, Object> menuTree = menuService.buildMenuTreeByUser(menuList);
        return ResultVOUtils.success(menuTree);
    }

    @RequestMapping("/sys/menu/add")
    public ResultVO addMenu(@RequestBody Menu menu) {
        try {
            menuService.add(menu);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }

        return ResultVOUtils.success();
    }

    @PutMapping("/sys/menu/update")
    public ResultVO updateMenu(@RequestBody Menu resources) {
        try {
            menuService.updateMenu(resources);
        } catch (DataNotFoundException dx) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), dx.getMsg());
        } catch (EntityExistException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMessage());
        }
        return ResultVOUtils.success();
    }

    @DeleteMapping("/sys/menu/delete/{menuId}")
    public ResultVO deleteMenu(@PathVariable Long menuId) {
        Set<Menu> menuSet = new HashSet<>();
        List<Menu> menuList = menuService.findByParentId(menuId);
        Menu menu = menuService.findOne(menuId);
        menuSet.add(menu);
        menuSet = menuService.getDeleteMenus(menuList, menuSet);
        if (menuSet.size() > 1) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), "该层级下有数据");
        }
        menuService.delete(menuSet);
        return ResultVOUtils.success();
    }

    @GetMapping("/sys/menu/getMenuById/{id}")
    public ResultVO getMenuById(@PathVariable Long id) {
        Menu menu = null;
        try {
            menu = menuService.findOne(id);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success(menu);
    }

    @ApiOperation("pc端加载路由")
    @PostMapping("/sys/menu/findRoute")
    public ResultVO findRoute(@RequestBody User user) {
        user.setPortFlag(true);
        Object object = menuService.findAllRouteByAppIdSort(user);
        return ResultVOUtils.success(object);
    }

    @ApiOperation("根据用户等级加载菜单")
    @GetMapping("/sys/menu/findMenuByUserLevelId")
    public ResultVO findMenuByUserLevelId() {
        Map<String, Object> menuTree = menuService.findMenuByGroupLevelId();
        return ResultVOUtils.success(menuTree);
    }

}
