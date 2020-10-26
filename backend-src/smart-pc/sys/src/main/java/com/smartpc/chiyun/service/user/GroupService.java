package com.smartpc.chiyun.service.user;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.sys.MenuDao;
import com.smartpc.chiyun.dao.user.*;
import com.smartpc.chiyun.model.user.*;
import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GroupService {
    @Autowired
    UserDao userDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserGroupDao userGroupDao;

    @Autowired
    GroupPermissionDao groupPermissionDao;

    @Autowired
    PermissionDao permissionDao;
    @Autowired
    MenuDao menuDao;
    @Autowired
    GroupMenuDao groupMenuDao;

    @Transactional
    public Group save(Group group) {
        Group save = groupDao.save(group);
        return save;
    }

    @Transactional
    public void delete(Long id) {
        groupDao.deleteById(id);
    }

    @Transactional
    public void deletes(List<Long> ids) {
        groupDao.deletes(ids);
    }

    public Page<Group> list(BaseQuery<Group> query) {
        Group entity = query.getEntity();
        //根据当前登录用户判定加载角色；1.超级管理员加载所有角色，2.组织管理员加载当前组织的角色
        User user = AppUtil.getCurrentUser();
        if (user != null) {
            if (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel())) {
                entity.setOrgId(user.getOrgId());
            }
        }
        // String[] levelArray;
  /*      //当前登录用户是超级管理员，可以给超级管理员和组织管理员赋予角色，组织管理员只能赋予组织角色
        if (entity.getUser() != null && StringUtil.isNotEmpty(entity.getUser().getLevel())) {
            //超级管理员可赋予超级管理员和组织管理员角色
            if (Group.ADMIN.equals(entity.getUser().getLevel())) {
                levelArray = new String[]{Group.ADMIN, Group.ORG};
                List<String> levelList = Stream.of(levelArray).collect(Collectors.toList());
                entity.setLevelList(levelList);
            } else if (Group.ORG.equals(entity.getUser().getLevel())) {
                //组织管理员客可赋予普通用户角色
                levelArray = new String[]{Group.ORG};
                List<String> levelList = Stream.of(levelArray).collect(Collectors.toList());
                entity.setLevelList(levelList);
            }
        }*/
        PageInfo pageInfo = query.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getCurrentPage() - 1, pageInfo.getPageSize(), new Sort(Sort.Direction.DESC, "dateCreated"));
        Page<Group> list = groupDao.findAll(new Specification<Group>() {
            @Override
            public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(entity.getGroupName())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("groupName"), "%" + entity.getGroupName() + "%"));
                }
                if (entity.getIds() != null && entity.getIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("id");
                    Predicate in = exp.in(entity.getIds()).not();
                    predicates.add(in);
                }
               /* if (entity.getLevelList() != null && entity.getLevelList().size() > 0) {
                    Expression<List<String>> exp = root.get("level");
                    Predicate in = exp.in(entity.getLevelList());
                    predicates.add(in);
                }*/
           /*     if (entity.getOrgIds() != null && entity.getOrgIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("orgId");
                    Predicate in = exp.in(entity.getOrgIds());
                    predicates.add(in);
                }*/
                if (!StringUtils.isEmpty(entity.getOrgId())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("orgId"), entity.getOrgId()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
        return list;
    }


    /**
     * 根据UserId获取所有的用户组
     *
     * @param userId
     * @return
     */
    public Set<Group> findAllByUserId(Long userId) {
        List<UserGroup> allByUserId = userGroupDao.findAllByUserId(userId);
        List<Long> ids = allByUserId.stream().map(UserGroup::getGroupId).collect(Collectors.toList());
        List<Group> groupList = groupDao.findByIdIn(ids);
        for (Group group : groupList) {
            loadGruopPermissions(group);
        }
        return new HashSet<>(groupList);
    }

    /**
     * 根据用户组加载权限
     */
    public void loadGruopPermissions(Group group) {
        Long groupId = group.getId();
        List<GroupPermission> groupPermissions = groupPermissionDao.findAllByGroupId(groupId);
        List<Long> permissionIds = groupPermissions.stream().map(GroupPermission::getPermissionId).collect(Collectors.toList());
        List<Permission> ps = permissionDao.findAllById(permissionIds);
        group.setPermissions(ps);
    }

}
