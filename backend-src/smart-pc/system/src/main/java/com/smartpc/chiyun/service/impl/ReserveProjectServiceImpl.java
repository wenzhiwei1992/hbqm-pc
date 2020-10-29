
package com.smartpc.chiyun.service.impl;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ReserveProjectDao;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.enums.DictEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.ReserveProject;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.query.ProjectQuery;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.service.ReserveProjectService;
import com.smartpc.chiyun.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReserveProjectServiceImpl implements ReserveProjectService {
    @Autowired
    ReserveProjectDao reserveProjectDao;

    @Autowired
    OrgDao orgDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    CodeExplainContentDao codeExplainContentDao;


    @Override
    public ReserveProject findReserveProjectById(Long id) {
        return reserveProjectDao.findReserveProjectById(id);
    }

    @Override
    public Page<ReserveProject> findAll(ProjectQuery projectQuery) {
        //单基地orgId
        Long orgIds = AppUtil.getCurrentUserOrgId();
        Pageable pageable = PageRequest.of(projectQuery.getPageInfo().getCurrentPage() - 1, projectQuery.getPageInfo().getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        User user = AppUtil.getCurrentUser();
        //数据隔离
        projectQuery.setOrgIds(user.getOrgIdsData());

        Page<ReserveProject> pList = reserveProjectDao.findAll(new Specification<ReserveProject>() {
            @Override
            public Predicate toPredicate(Root<ReserveProject> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.in(root.get("orgId")).value(orgIds));
                if (projectQuery.getProjectNo() != null && !projectQuery.getProjectNo().equals("")) {
                    predicates.add(criteriaBuilder.like(root.get("projectNo").as(String.class), "%" + projectQuery.getProjectNo() + "%"));
                }
                if (StringUtil.isNotNullAndEmpty(projectQuery.getApprovalStatus())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("approvalStatus"), projectQuery.getApprovalStatus() ));
                }
                if (projectQuery.getProvinceId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("provinceId"), projectQuery.getProvinceId() ));
                }

                if (projectQuery.getCityId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("cityId"), projectQuery.getCityId() ));
                }

                //predicates.add(criteriaBuilder.equal(root.get("orgId").as(Long.class), orgId));
                if (projectQuery.getOrgIds() != null && projectQuery.getOrgIds().size() > 0) {
                    Expression<List<Long>> exp = root.get("orgId");
                    Predicate in = exp.in(projectQuery.getOrgIds());
                    predicates.add(in);
                }
                List<Predicate> list = new ArrayList<>();
                Predicate[] pre = new Predicate[predicates.size()];
                criteriaQuery.where(predicates.toArray(pre));
                return criteriaBuilder.and(predicates.toArray(pre));
            }
        }, pageable);
        User u = AppUtil.getCurrentUser();
        List<ReserveProject> content = pList.getContent();
        List<CodeExplainContent> codes =  codeExplainContentDao.findAllByCodeNoOrderBySort(DictEnum.RESERVE_PROJECT_APPROVAL_STATE.name());

        for (ReserveProject g : content) {
            if(g.getProvinceId()!=null){
                Department d =  departmentDao.findDepartmentById(g.getProvinceId());
                if(d!=null){
                    g.setProvinceName(d.getDeptName());
                }
            }
            if(g.getCityId()!=null){
                Department d =  departmentDao.findDepartmentById(g.getCityId());
                if(d!=null){
                    g.setCityName(d.getDeptName());
                }
            }
            if(codes.size()==0  || codes.get(codes.size()-1).getCodeName().equals(g.getApprovalStatus())){
                g.setIsApproval(false);
             } else {
                if (User.ADMIN.equals(user.getLevel())) { // 超级管理员查看全部
                  g.setIsApproval(true);
                } else if (User.ORG.equals(user.getLevel())) { // 省市管理员看对应的省市
                  if (user.getDeptId().equals(g.getApprovalDeptId())) {
                    g.setIsApproval(true);
                  } else {
                    g.setIsApproval(false);
                  }
                } else { // 普通用户
                  g.setIsApproval(false);
                }
            }
        }
        return pList;
    }

    /**
     * 判断项目编号是否已存在
     *
     * @param proejctNo
     * @throws DataNotFoundException
     */
    @Override
    public void judgeProjectNoPresence(String proejctNo) throws DataNotFoundException {
        Long orgId = AppUtil.getCurrentUserOrgId();
        List<ReserveProject> projectList = reserveProjectDao.findByProjectNo(proejctNo, orgId);
        if (projectList.size() > 0) {
            throw new DataNotFoundException("编号已存在");
        }
    }



    @Override
    public void deleteReserveProjectById(Long id) {
        reserveProjectDao.deleteById(id);
    }


    @Override
    public List<ReserveProject> findAll() {
        return reserveProjectDao.findAllByOrgIds(AppUtil.getCurrentUser().getOrgIdsData());
    }



}
