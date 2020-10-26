package com.smartpc.chiyun.service.user;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.config.PageInfo;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.dao.user.UserDao;
import com.smartpc.chiyun.model.BaseQuery;
import com.smartpc.chiyun.model.user.Org;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class OrgService {
    @Autowired
    OrgDao orgDao;
    @Autowired
    UserDao userDao;

    @Transactional
    public Org save(Org entity) {
        Org save = orgDao.save(entity);
        return save;
    }

    @Transactional
    public void delete(Long id) {
        orgDao.deleteById(id);
    }

    @Transactional
    public void deletes(List<Long> ids) {
        orgDao.deletes(ids);
    }

    public Page<Org> list(BaseQuery<Org> query) {
        Org entity = query.getEntity();
        User user = AppUtil.getCurrentUser();
        //数据隔离
        if (User.GENERAL.equals(user.getLevel()) || User.ORG.equals(user.getLevel())) {
            entity.setOrgIds(user.getOrgIdsData());
        }

        PageInfo pageInfo = query.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getCurrentPage() - 1, pageInfo.getPageSize(), new Sort(Sort.Direction.ASC, "orgName"));
        Page<Org> list = orgDao.findAll(new Specification<Org>() {
            @Override
            public Predicate toPredicate(Root<Org> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtil.isNotNullAndEmpty(entity.getApprovalStatus())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("approvalStatus"), entity.getApprovalStatus() ));
                }
                if (StringUtil.isNotNullAndEmpty(entity.getProductionType())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("productionType"), entity.getProductionType() ));
                }

                if (StringUtil.isNotNullAndEmpty(entity.getState())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("state"), entity.getState() ));
                }

                if (entity.getId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("id"), entity.getId() ));
                }
                if (entity.getProvinceId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("provinceId"), entity.getProvinceId() ));
                }

                if (entity.getCityId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("cityId"), entity.getCityId() ));
                }

                if (!StringUtils.isEmpty(entity.getProposerPhone())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("proposerPhone"), "%" + entity.getProposerPhone() + "%"));
                }
                if (!StringUtils.isEmpty(entity.getProposer())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("proposer"), "%" + entity.getProposer() + "%"));
                }
                if (entity.getIds() != null && entity.getIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("id");
                    Predicate in = exp.in(entity.getIds()).not();
                    predicates.add(in);
                }
                if (entity.getOrgIds() != null && entity.getOrgIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("id");
                    Predicate in = exp.in(entity.getOrgIds());
                    predicates.add(in);
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
        return list;
    }

    public List<User> orgUserList(Long orgId) {
        User u = new User();
        u.setOrgId(orgId);
        List<User> us = userDao.findAll(Example.of(u));
        return us;
    }

    public void removeUser(Long userId, Long orgId) {
        User user = userDao.findByIdAndOrgId(userId, orgId);
        if (user != null) {
            user.setOrgId(null);
            userDao.save(user);
        }
    }

    public void addUsers(List<User> list) {
        for (User user : list) {
            userDao.findById(user.getId()).ifPresent(new Consumer<User>() {
                @Override
                public void accept(User entity) {
                    entity.setOrgId(user.getOrgId());
                    userDao.save(entity);
                }
            });
        }
    }

    public List<Org> getOrgNotTrue() {
        return orgDao.findOrgsNotTrue();
    }

    /**
     * 查询组织根据当前登录用户；超级管理员查询所有组织，组织管理员查询当前组织
     *
     * @return
     */
    public List<Org> findOrgByUser() {
        List<Org> orgList = new ArrayList<>();
        User user = AppUtil.getCurrentUser();
        if (user != null) {
            if (User.ADMIN.equals(user.getLevel())) {
                orgList = orgDao.findAllOrg();
            } else if (User.ORG.equals(user.getLevel()) || User.GENERAL.equals(user.getLevel())) {
                orgList.add(orgDao.findOrgById(user.getOrgId()));
            }
        }
        return orgList;
    }


}
