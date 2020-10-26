package com.smartpc.chiyun.service.user.impl;


import com.smartpc.chiyun.dao.sys.MenuDao;
import com.smartpc.chiyun.dao.user.GroupDao;
import com.smartpc.chiyun.dao.user.GroupMenuDao;
import com.smartpc.chiyun.model.sys.Menu;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.GroupMenu;
import com.smartpc.chiyun.service.user.GroupMenuService;
import com.smartpc.chiyun.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author yue
 * @create 2020/3/17 11:08 下午
 */
@Service
public class GroupMenuServiceImpl implements GroupMenuService {
    @Autowired
    GroupDao groupDao;
    @Autowired
    MenuDao menuDao;
    @Autowired
    GroupMenuDao groupMenuDao;

    @Transactional
    @Override
    public void updateGroupPermission(Group resources) {
        ValidationUtil.isNull(resources.getGroupName(), "名称", "id", resources.getId());
        ValidationUtil.isNull(resources.getAuthorities(), "权限", "id", resources.getId());
        Group query = groupDao.findById(resources.getId()).get();
        ValidationUtil.isNull(query, "记录", "id", resources.getId());
        //角色原有权限
        List<GroupMenu> groupMenuList = groupMenuDao.findGroupMenusByGroupId(resources.getId());
        //角色现有权限
        String[] split = resources.getAuthorities().split(";");
        List<String> menuIdList = Stream.of(split).collect(Collectors.toList());
        List<Menu> menus = menuDao.findMenuIdByIndex(menuIdList);
        //1.角色权限新增
        //2.角色权限减少
        //3.角色权限更新
        if (groupMenuList.size() > 0) {
            Set<Long> result1 = new HashSet<>();
            Set<Long> result2 = new HashSet<>();
            Set<Long> menuIdListOld = groupMenuList.stream().map(GroupMenu::getMenuId).collect(Collectors.toSet());
            Set<Long> menuIdListNew = menus.stream().map(Menu::getId).collect(Collectors.toSet());
            //取删除的角色权限
            result1.addAll(menuIdListOld);
            result1.removeAll(menuIdListNew);
            //取新增的角色权限
            result2.addAll(menuIdListNew);
            result2.removeAll(menuIdListOld);
            if (result1.size() > 0) {
                groupMenuDao.deleteGroupMenuByMenuIdsAndGroupId(result1, resources.getId());
            }
            if (result2.size() > 0) {
                List<GroupMenu> groupMenus = new ArrayList<>();
                GroupMenu groupMenu = null;
                for (Long id : result2) {
                    groupMenu = new GroupMenu();
                    groupMenu.setGroupId(resources.getId());
                    groupMenu.setMenuId(id);
                    groupMenus.add(groupMenu);
                }
                groupMenuDao.saveAll(groupMenus);

            }
        } else if (groupMenuList.size() == 0) {
            List<GroupMenu> groupMenus = new ArrayList<>();
            GroupMenu groupMenu = null;
            for (Menu menu : menus) {
                groupMenu = new GroupMenu();
                groupMenu.setGroupId(resources.getId());
                groupMenu.setMenuId(menu.getId());
                groupMenus.add(groupMenu);
            }
            groupMenuDao.saveAll(groupMenus);
        }

    }

    @Override
    public List<GroupMenu> findGroupMenusByGroupId(Long groupId) {
        return groupMenuDao.findGroupMenusByGroupId(groupId);
    }

    @Override
    public List<String> findIndexByGroupId(Long groupId) {
        List<GroupMenu> groupMenuList = groupMenuDao.findGroupMenusByGroupId(groupId);
        List<String> stringList = new ArrayList<>();
        if (groupMenuList.size() > 0) {
            List<Long> menuIds = groupMenuList.stream().map(GroupMenu::getMenuId).collect(Collectors.toList());
            List<Menu> menuList = menuDao.findMenuByIds(menuIds);
            menuList.forEach(menu -> {
                stringList.add(menu.getIndex());
            });
        } else {
            // throw new DataNotFoundException("请检查角色权限，角色 ID ：" + groupId);
        }

        return stringList;
    }

    @Override
    public void deleteGroupMenuByGroupId(Long groupId) {
        groupMenuDao.deleteGroupMenuByGroupId(groupId);
    }
}
