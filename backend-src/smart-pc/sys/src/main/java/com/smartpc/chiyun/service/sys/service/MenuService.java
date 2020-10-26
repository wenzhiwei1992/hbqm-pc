package com.smartpc.chiyun.service.sys.service;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.enums.DictEnum;
import com.smartpc.chiyun.model.syscode.CodeExplain;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.service.sys.dto.MenuDto;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.dao.sys.MenuDao;
import com.smartpc.chiyun.dao.user.GroupMenuDao;
import com.smartpc.chiyun.dao.user.UserGroupDao;
import com.smartpc.chiyun.model.sys.Menu;
import com.smartpc.chiyun.model.user.Group;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.service.sys.dto.MenuQueryCriteria;
import com.smartpc.chiyun.service.sys.mapper.MenuMapper;
import com.smartpc.chiyun.service.syscode.CodeExplainContentService;
import com.smartpc.chiyun.service.syscode.CodeExplainService;
import com.smartpc.chiyun.service.user.UserGroupService;
import com.smartpc.chiyun.exception.EntityExistException;
import com.smartpc.chiyun.utils.QueryHelp;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuService {
    @Autowired
    MenuDao menuDao;
    @Autowired
    GroupMenuDao groupMenuDao;
    @Autowired
    UserGroupDao userGroupDao;
    @Autowired
    UserGroupService userGroupService;
    @Autowired
    FileService fileService;
    @Autowired
    CodeExplainContentService codeExplainContentService;

    private final MenuMapper menuMapper;

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }


    public List<Menu> getMenuListByQuery(MenuDto menuDto) {
        List<Menu> menuList = menuDao.findAll(new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(menuDto.getType())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("type"), menuDto.getType()));
                }
                if (!StringUtils.isEmpty(menuDto.getAppId())) {
                    predicates.add(criteriaBuilder.equal(root.<Integer>get("appId"), menuDto.getAppId()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        return menuList;
    }

    /**
     * pc端app端用户菜单加载
     *
     * @param user
     * @return
     */
    public Map<String, Object> getMenuByLevel(User user) {
        Map<String, Object> map = new HashMap<>();
        //不论是什么等级的用户都要以用户对应的角色菜单为主
        //超级管理员默认加载权限管理菜单（方案暂定）
        List<Group> groupList = userGroupService.userGroupList(user.getId());
        if (groupList.size() == 0 && User.ADMIN.equals(user.getLevel())) {
            //if (User.ADMIN.equals(user.getLevel())) {
            //超级管理员
            //加载menu表中appId为0的菜单
            List<Menu> menuList = menuDao.findMenuByHiddenAndFlag(user.getPortFlag());
            List<Menu> menuByCodeNo = menuDao.findMenuByIds(this.getMenuByCodeNo());
            menuList.addAll(menuByCodeNo);
            map = this.buildMenuTreeByUser(menuList);
        } else if (groupList != null && groupList.size() > 0) {
            //} else
            // if (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel())) {
            //单组织管理员   普通用户
            // List<Menu> menuList = menuDao.findAllMenuByAppIdSort(1);
            //this.buildMenuTreeByUser(menuList);
            //根据当前用户获取角色信息
            //根据角色加载菜单
            if (groupList.size() > 0) {
                Set<Long> groupIdList = groupList.stream().map(Group::getId).collect(Collectors.toSet());
                //加载角色对应的所有菜单
                List<Long> longList = groupMenuDao.findByGroupIdsAndSortAsc(groupIdList, user.getPortFlag());
                if (longList.size() > 0) {
                    //根据菜单ID查询菜单信息
                    longList.addAll(this.getMenuByCodeNo());
                    List<Menu> menuList = menuDao.findMenuByIds(longList);
                    map = this.buildMenuTreeByUser(menuList);
                }
            }


        }/* else if (User.GENERAL.equals(user.getLevel())) {
            //普通用户
        }*/

        return map;

    }

    public Map<String, Object> buildMenuTreeByAppId(Integer appId) {
        Map<String, Object> map = new HashMap<>();
        List<Menu> menuList = menuDao.findAllMenuByAppIdSort(appId);
        map = this.buildMenuTreeByUserNew(menuList);
        return map;
    }

    public Map<String, Object> buildMenuTreeByUserNew(List<Menu> menus) {
        List<Menu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (Menu menu : menus) {
            //如果为false 则返回json不包含该属性
            if (menu.getAuthMark() ==null || !menu.getAuthMark()) {
                menu.setAuthMark(null);
            }
            if (menu.getParentId() == 0) {
                trees.add(menu);
            }
            for (Menu it : menus) {
                if (it.getParentId().equals(menu.getId())) {
                    if (menu.getMenuItems() == null) {
                        menu.setMenuItems(new ArrayList<>());
                    }
                    menu.getMenuItems().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
       /* List<Menu> menuListApp = trees.stream().filter(menu -> menu.getFlag() == false).collect(Collectors.toList());
        List<Menu> menuListPc = trees.stream().filter(menu -> menu.getFlag() == true).collect(Collectors.toList());
        Menu menu = new Menu();
        menu.setMenuItems(menuListApp);
        menu.setTitle("APP权限");
        menu.setAuthMark(null);
        //0已被前端占用，取一个唯一值
        menu.setId(999999L);
        menu.setIndex("");
        menuListPc.add(menu);*/
        map.put("content", trees);
        map.put("totalElements", trees.size());
        return map;
    }

    public Map<String, Object> buildMenuTreeByUser(List<Menu> menus) {
        List<Menu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (Menu menu : menus) {
            //如果为false 则返回json不包含该属性
            if (!menu.getAuthMark()) {
                menu.setAuthMark(null);
            }
            if (menu.getParentId() == 0) {
                trees.add(menu);
            }
            for (Menu it : menus) {
                if (it.getParentId().equals(menu.getId())) {
                    if (menu.getMenuItems() == null) {
                        menu.setMenuItems(new ArrayList<>());
                    }
                    menu.getMenuItems().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        for (int i = 0; i < trees.size(); i++) {
            if ("permission".equals(trees.get(i).getIndex()) || "management".equals(trees.get(i).getIndex())) {
                trees.get(i).setIcon(fileService.addNetPath(trees.get(i).getIcon()));
            }
        }
        map.put("content", trees);
        map.put("totalElements", trees.size());
        return map;
    }


    public Map<String, Object> buildMenuTree() {
        List<Menu> menus = menuDao.findAllMenuBySort();
        List<Menu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (Menu menu : menus) {
            //如果为false 则返回json不包含该属性
            if (!menu.getAuthMark()) {
                menu.setAuthMark(null);
            }
            if (menu.getParentId() == 0) {
                trees.add(menu);
            }
            for (Menu it : menus) {
                if (it.getParentId().equals(menu.getId())) {
                    if (menu.getMenuItems() == null) {
                        menu.setMenuItems(new ArrayList<>());
                    }
                    menu.getMenuItems().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content", trees);
        map.put("totalElements", trees.size());
        return map;
    }

    public void add(Menu menu) {
        if (StringUtil.isNotEmpty(menu.getType()) && !Menu.MENU_TYPE_ML.equals(menu.getType())) {
            ValidationUtil.isNull(menu.getParentId(), "上级目录", "id", null);
        }
        ValidationUtil.isNull(menu.getType(), "类型", "id", null);
        if (Menu.MENU_TYPE_ML.equals(menu.getType()) || Menu.MENU_TYPE_CD.equals(menu.getType())) {
            //一级目录getParentId==0
            //二级目录判定
            if (menu.getParentId() != 0) {
                Menu m = menuDao.findById(menu.getParentId()).orElseGet(Menu::new);
                if (!Menu.MENU_TYPE_ML.equals(m.getType())) {
                    throw new DataNotFoundException("上级类目请选目录类型");
                }
                menuDao.save(menu);
            }
            if (Menu.MENU_TYPE_CD.equals(menu.getType()) && 0 == menu.getParentId().longValue()) {
                throw new DataNotFoundException("请选择正确的目录类型");
                //Menu.MENU_TYPE_ML.equals(menu.getType()) &&
            } else if (Menu.MENU_TYPE_ML.equals(menu.getType()) && 0 == menu.getParentId().longValue()) {
                menuDao.save(menu);
            }
        } else if (Menu.MENU_TYPE_AN.equals(menu.getType())) {
            if (menu.getParentId() == 0) {
                throw new DataNotFoundException("上级类目请选授权按钮权限的菜单类型");
            }
            Menu m = menuDao.findById(menu.getParentId()).orElseGet(Menu::new);
            if (Menu.MENU_TYPE_CD.equals(m.getType()) && m.getAuthMark() == true) {
                menuDao.save(menu);
            } else {
                throw new DataNotFoundException("上级类目请选授权按钮权限的菜单类型");
            }
        }
    }

    public void updateMenu(Menu resources) throws DataNotFoundException {
        if (resources.getId().equals(resources.getParentId())) {
            throw new DataNotFoundException("上级不能为自己");
        }
        Menu menu = menuDao.findById(resources.getId()).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(), "IndexName", "id", resources.getId());
        Menu menu1 = menuDao.findByTitleAndAppId(resources.getTitle(), resources.getAppId());
        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new EntityExistException(Menu.class, "title", resources.getTitle());
        }
        if (!menu.getType().equals(resources.getType()) || !menu.getParentId().equals(resources.getParentId())) {
            List<Menu> menuList = this.findByParentId(resources.getId());
            Set<Menu> menuSet = new HashSet<>();
            Set<Menu> menuSet1 = this.getDeleteMenus(menuList, menuSet);
            if (menuSet1.size() > 0) {
                throw new DataNotFoundException("该数据下存在对应数据，不可改变层级");
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setIcon(resources.getIcon());
        menu.setParentId(resources.getParentId());
        menu.setSort(resources.getSort());
        menu.setAuthMark(resources.getAuthMark());
        menu.setIndex(resources.getIndex());
        menu.setIFrame(resources.getIFrame());
        menu.setIsRoute(resources.getIsRoute());
        menu.setIsTab(resources.getIsTab());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setAppId(resources.getAppId());
        menu.setFlag(resources.getFlag());
        menu.setHidden(resources.getHidden());
        menu.setName(resources.getName());
        menuDao.save(menu);
    }

    public List<Menu> findByParentId(long pid) {
        return menuDao.findByParentId(pid);
    }

    public Menu findOne(Long id) {
        Menu menu = menuDao.findById(id).orElseGet(Menu::new);
        ValidationUtil.isNull(menu.getId(), "Menu", "id", id);
        return menu;
    }

    public Set<Menu> getDeleteMenus(List<Menu> menuList, Set<Menu> menuSet) {
        // 递归找出待删除的菜单
        for (Menu menu1 : menuList) {
            menuSet.add(menu1);
            List<Menu> menus = menuDao.findByParentId(menu1.getId());
            if (menus != null && menus.size() != 0) {
                getDeleteMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    @Transactional
    public void delete(Set<Menu> menuSet) {
        for (Menu menu : menuSet) {
            groupMenuDao.untiedMenu(menu.getId());
            menuDao.deleteById(menu.getId());
        }
    }

    public List<MenuDto> queryAll(MenuQueryCriteria criteria) {
        return menuMapper.toDto(menuDao.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    /**
     * 加载pc端路由
     *
     * @param user
     * @return
     */
    public Object findAllRouteByAppIdSort(User user) {
        Object object = null;
        //if (User.ADMIN.equals(user.getLevel())) {
        //超级管理员
        List<Group> groupList = userGroupService.userGroupList(user.getId());
        if (groupList.size() == 0 && User.ADMIN.equals(user.getLevel())) {
            //加载menu表中appId为0的菜单
            List<Menu> menuList = menuDao.findMenuByHiddenAndFlagAndIsRoute(user.getPortFlag());
            List<Menu> longHiddenList = menuDao.findMenuByHidden(true);
            menuList.addAll(longHiddenList);
            object = this.buildRouteTreeByUser(menuList);
        } else if (groupList != null && groupList.size() > 0) {
            //if (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel())) {
            //} else
            //根据角色加载菜单
            //List<Group> groupList = userGroupService.userGroupList(user.getId());
            if (groupList.size() > 0) {
                Set<Long> groupIdList = groupList.stream().map(Group::getId).collect(Collectors.toSet());
                //加载角色对应的所有路由,不包含隐藏（路由）菜单
                List<Long> longList = groupMenuDao.findRouteByGroupIdsAndSortAsc(groupIdList, user.getPortFlag());
                List<Long> longHiddenList = menuDao.findMenuByHidden();
                if (longList.size() > 0) {
                    longList.addAll(longHiddenList);
                    //根据菜单ID查询菜单信息
                    List<Long> longs = new ArrayList<>();
                    longs = longList.stream().distinct().collect(Collectors.toList());
                    List<Menu> menuList = menuDao.findMenuByIds(longs);
                    //菜单统一加上 menu.setRedirect("noredirect")；用于前端动态路由判定
                   /* Map<Long, List<Menu>> mapRoute = menuList.stream().collect(Collectors.groupingByConcurrent(k -> k.getParentId()));
                    for (int i = 0; i < menuList.size(); i++) {
                        Menu menu = menuList.get(i);
                        List<Menu> menus = mapRoute.get(menu.getId());
                        if (menus != null && menus.size() > 0) {
                            menu.setRedirect("noredirect");
                        }
                    }*/
             /*       List<Menu> menuList1 = menuList.stream().filter(menu -> Menu.MENU_TYPE_CD.equals(menu.getType())).collect(Collectors.toList());
                    for (int i = 0; i < menuList1.size(); i++) {
                        Menu menu = menuList1.get(i);
                        if (menu.getComponent().length() > 6) {
                            menu.setComponent(menu.getComponent().substring(6));
                        }
                    }
                    menuDao.saveAll(menuList1);*/
                    object = this.buildRouteTreeByUser(menuList);
                }
            }
        }
        return object;
    }

    /**
     * 路由按等级加载
     *
     * @param menus
     * @return
     */
    public Object buildRouteTreeByUser(List<Menu> menus) {
        List<Menu> menuButtonList = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            if (menu != null &&
                    menu.getIsRoute() == true && Menu.MENU_TYPE_AN.equals(menu.getType())) {
                menuButtonList.add(menu);
            }
        }
        menus = menus.stream().filter(menu -> !menuButtonList.contains(menu)).collect(Collectors.toList());
        List<Menu> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (Menu menu : menus) {
            //如果为false 则返回json不包含该属性
            if (!menu.getAuthMark()) {
                menu.setAuthMark(null);
            }
            if (menu.getParentId() == 0) {
                trees.add(menu);
            }
            for (Menu it : menus) {
                if (it.getParentId().equals(menu.getId())) {
                    if (menu.getMenuItems() == null) {
                        menu.setMenuItems(new ArrayList<>());
                    }
                    menu.getMenuItems().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        if (menuButtonList.size() > 0) {
            Menu menu = new Menu();
            menu.setMenuItems(menuButtonList);
            menu.setTitle("按钮集合");
            menu.setAuthMark(null);
            menu.setId(0L);
            menu.setIndex("");
            menu.setPath("/Main");
            menu.setComponent("Main");
            // trees.add(menu);
            for (int i = 0; i < trees.size(); i++) {
                if ("permission".equals(trees.get(i).getIndex()) || "management".equals(trees.get(i).getIndex())) {
                    trees.get(i).getMenuItems().add(menu);
                }
            }
        }
        return trees;
    }

    public Map<String, Object> findMenuByGroupLevelId() {
        Map<String, Object> map = new HashMap<>();
        User user = AppUtil.getCurrentUser();
        if (user != null && User.ADMIN.equals(user.getLevel())) {
            //map = this.buildMenuTreeByAppId(1);
            List<Menu> menuList = menuDao.findMenuByHidden(false);
            map = this.buildMenuTreeByUserNew(menuList);
        }
        // if (user != null && (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel()))) {
        List<Group> groupList = userGroupService.userGroupList(user.getId());
        if (groupList.size() > 0) {
            Set<Long> groupIdList = groupList.stream().map(Group::getId).collect(Collectors.toSet());
            //加载角色对应的所有菜单
            List<Long> longList = groupMenuDao.findByGroupIdsAndSortAsc(groupIdList, 1);
            if (longList.size() > 0) {
                //根据菜单ID查询菜单信息
                List<Menu> menuList = menuDao.findMenuByIds(longList);
                map = this.buildMenuTreeByUserNew(menuList);
            }
        }

        //}
        return map;
    }

    public List<Long> getMenuByCodeNo() {
        //根据生产方式字典加载菜单
        List<Long> idList = new ArrayList<>();
        List<CodeExplainContent> codeExplainList = codeExplainContentService.findAllByCodeNo(DictEnum.PRODUCTION.name());
        for (int i = 0; i < codeExplainList.size(); i++) {
            if ("PR".equals(codeExplainList.get(i).getRemark())) {
                List<String> list = new ArrayList<>();
                list.add("orderManageNoProject:addClickFB");
                if (codeExplainList.get(i).getIsEnabled()) {
                    list.add("orderManageNoProject:addClickP");
                }
                List<Menu> mList = menuDao.findMenuIdByIndex(list);
                idList = mList.stream().map(Menu::getId).collect(Collectors.toList());
                list = null;
            }
        }
        return idList;
    }

}
