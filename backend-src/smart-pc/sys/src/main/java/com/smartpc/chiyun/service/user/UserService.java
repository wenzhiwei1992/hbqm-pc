package com.smartpc.chiyun.service.user;


import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.user.*;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.user.Org;
import com.smartpc.chiyun.model.user.UserGroup;
import com.smartpc.chiyun.utils.StringUtil;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.model.user.UserM2EpWx;
import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.model.sys.VTUserDX;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    UserM2EpWxDao userM2EpWxDao;

    @Autowired
    UserGroupService userGroupService;
    @Autowired
    OrgDao orgDao;
    @Autowired
    UserGroupDao userGroupDao;

    @Autowired
    DepartmentDao departMentDao;

    @Transactional
    public User save(User entity) {
        User save = userDao.save(entity);
        return save;
    }

    @Transactional
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Transactional
    public void deletes(List<Long> ids) {
        userDao.deletes(ids);
    }

    public Page<User> list(BaseQuery<User> query) {
        PageInfo pageInfo = query.getPageInfo();
        User entity = query.getEntity();
        String[] levelArray;
        String level = AppUtil.getCurrentUserLevel();
        if (User.ORG.equals(level)) {
            levelArray = new String[]{User.ORG, User.GENERAL};
            List<String> levelList = Stream.of(levelArray).collect(Collectors.toList());
            entity.setLevelList(levelList);
        } else if (User.GENERAL.equals(level)) {
            levelArray = new String[]{User.GENERAL};
            List<String> levelList = Stream.of(levelArray).collect(Collectors.toList());
            entity.setLevelList(levelList);
        } else if (User.ADMIN.equals(level)) {
            levelArray = new String[]{User.ADMIN, User.ORG};
            List<String> levelList = Stream.of(levelArray).collect(Collectors.toList());
            entity.setLevelList(levelList);
        }
        Pageable pageable = PageRequest.of(pageInfo.getCurrentPage() - 1, pageInfo.getPageSize(), new Sort(Sort.Direction.DESC, "dateCreated"));
        Page<User> list = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(entity.getMobile())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("mobile"), "%" + entity.getMobile() + "%"));
                }
                if (!StringUtils.isEmpty(entity.getUsername())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("username"), "%" + entity.getUsername() + "%"));
                }
                if (!StringUtils.isEmpty(entity.getRealName())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("realName"), "%" + entity.getRealName() + "%"));
                }
                if (!StringUtils.isEmpty(entity.getOrgId())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("orgId"), entity.getOrgId()));
                }
                if (entity.getDeptId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("deptId"), entity.getDeptId()));
                }
                if (entity.isOrgIdNull()) {
                    predicates.add(root.get("orgId").isNull());
                }
                if (entity.getIds() != null && entity.getIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("id");
                    Predicate in = exp.in(entity.getIds()).not();
                    predicates.add(in);
                }
                if (entity.getLevelList() != null && entity.getLevelList().size() > 0) {
                    Expression<List<String>> exp = root.get("level");
                    Predicate in = exp.in(entity.getLevelList());
                    predicates.add(in);
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
        List<User> content = list.getContent();
        for (User u : content) {
            u.setGroups(userGroupService.userGroupList(u.getId()));
            if(u.getDeptId()!=null){
                u.setDeptName(getDeptName(u.getDeptId()));
            }
            Long orgId = u.getOrgId();
            if (orgId != null) {
                orgDao.findById(orgId).ifPresent(new Consumer<Org>() {
                    @Override
                    public void accept(Org org) {
                        u.setOrg(org);
                    }
                });
            }
        }
        return list;
    }


    public String getDeptName(long deptId) {
        Department d = departMentDao.findDepartmentById(deptId);
        if(d!=null){
            return d.getDeptName();
        }
        return "";
    }

    public String getName(long id) {
        String name = id + "";
        Optional<User> u = userDao.findById(id);
        if (!u.isPresent()) {
            return name;
        }
        User user = u.get();
        if (!StringUtils.isEmpty(user.getRealName())) {
            name = user.getRealName();
        } else if (!StringUtils.isEmpty(user.getUsername())) {
            name = user.getUsername();
        }
        return name;
    }


    /**
     * 保存企业微信的用户
     *
     * @param deptUsers
     */
    public void saveQywxUsers(List<VTUserDX> deptUsers) {
        List<User> list = new ArrayList<>();
        for (VTUserDX deptUser : deptUsers) {
            String userid = deptUser.getUserid();
            String avatar = deptUser.getAvatar();
            String department = deptUser.getDepartment();
            String enable = deptUser.getEnable();
            String name = deptUser.getName();
            String mobile = deptUser.getMobile();
            String email = deptUser.getEmail();
            if (StringUtil.isNotEmpty(enable) && enable.equals("0")) {
                continue;
            }
            User user = null;
            List<User> byUsername = userDao.findByUsernameList(userid);
            if (byUsername.size() > 0) {
                user = byUsername.get(0);
                user.setMobile(mobile);
                user.setNickName(name);
                user.setRealName(name);
                user.setAvatar(avatar);
                user.setEmail(email);
            } else {
                user = new User();
                user.setUsername(userid);
                user.setPassword(User.DEFAULT_PASSWORD);
                user.setMobile(mobile);
                user.setOrgId(AppUtil.getCurrentUserOrgId());
                user.setNickName(name);
                user.setRealName(name);
                user.setAvatar(avatar);
                user.setEmail(email);
            }
            //企业微信同步用户默认等级为普通用户
            user.setLevel(User.GENERAL);
            list.add(user);
        }

        userDao.saveAll(list);

    }

    public List<User> findByUsername(String username) {
        return userDao.findByUsernameList(username);
    }

    /**
     * 同步的所有用户,要同时同步到映射表,方便配置
     *
     * @param deptUsers
     */
    public void saveQywxUsersToMappTable(List<VTUserDX> deptUsers) {
        List<UserM2EpWx> list = new ArrayList<>();
        UserM2EpWx user = null;
        for (VTUserDX deptUser : deptUsers) {
            String userid = deptUser.getUserid();
            String enable = deptUser.getEnable();
            if (StringUtil.isNotEmpty(enable) && enable.equals("0")) {
                continue;
            }
            user = new UserM2EpWx();
            User user1 = userDao.findByUsername(userid);
            user.setUserId(user1 == null ? userid : user1.getId() + "");
            user.setEpWxUserId(userid);
            user.setOrgId(AppUtil.getCurrentUserOrgId());
            user.setType(1);
            list.add(user);
        }

        userM2EpWxDao.saveAll(list);
    }

    public User findUserById(Long id) {
        return userDao.findById(id).get();
    }

    public User updateUser(User entity) {
        User query = userDao.findById(entity.getId()).get();
        if (query == null) {
            throw new DataNotFoundException("用户不存在");
        }
        String username = entity.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new DataNotFoundException("用户名不能为空");
        }
        if (StringUtils.isEmpty(entity.getMobile())) {
            throw new DataNotFoundException("手机号不能为空");
        }
        String realName = entity.getRealName();
        if (StringUtils.isEmpty(realName)) {
            throw new DataNotFoundException("用户真实姓名不能为空");
        }


        query.setRealName(realName);
        query.setNickName(entity.getNickName());
        query.setMobile(entity.getMobile());
        query.setEmail(entity.getEmail());
        query.setTelephone(entity.getTelephone());
        query.setOrgId(entity.getOrgId());
        query.setOrgIds(entity.getOrgIds());
        query.setLevel(entity.getLevel());
        query.setDeptId(entity.getDeptId());
        User save = userDao.save(query);
        return save;
    }
}
