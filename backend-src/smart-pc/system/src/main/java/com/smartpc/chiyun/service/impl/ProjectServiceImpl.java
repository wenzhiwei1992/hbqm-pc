
package com.smartpc.chiyun.service.impl;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ProjectDao;
import com.smartpc.chiyun.dao.dept.DepartmentDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.enums.DictEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.dept.Department;
import com.smartpc.chiyun.model.query.ProjectQuery;
import com.smartpc.chiyun.model.syscode.CodeExplainContent;
import com.smartpc.chiyun.model.user.User;
import com.smartpc.chiyun.service.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectDao projectDao;

    @Autowired
    OrgDao orgDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    CodeExplainContentDao codeExplainContentDao;

    @Override
    public Project findProjectById(Long id) {
        return projectDao.findProjectById(id);
    }

    @Override
    public Page<Project> findAll(ProjectQuery projectQuery) {
        //单基地orgId
        Long orgIds = AppUtil.getCurrentUserOrgId();
        Pageable pageable = PageRequest.of(projectQuery.getPageInfo().getCurrentPage() - 1, projectQuery.getPageInfo().getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        User user = AppUtil.getCurrentUser();
        //数据隔离
        projectQuery.setOrgIds(user.getOrgIdsData());

        Page<Project> pList = projectDao.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.in(root.get("orgId")).value(orgIds));
                if (projectQuery.getProjectName() != null && !projectQuery.getProjectName().equals("")) {
                    predicates.add(criteriaBuilder.like(root.get("projectName").as(String.class), "%" + projectQuery.getProjectName() + "%"));
                }
                if (projectQuery.getCustomer() != null && !projectQuery.getCustomer().equals("")) {
                    predicates.add(criteriaBuilder.like(root.get("customer").as(String.class), "%" + projectQuery.getCustomer() + "%"));
                }
                if (projectQuery.getWorkPlace() != null && !projectQuery.getWorkPlace().equals("")) {
                    predicates.add(criteriaBuilder.like(root.get("workPlace").as(String.class), "%" + projectQuery.getWorkPlace() + "%"));
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

        List<Project> content = pList.getContent();
        List<CodeExplainContent> codes =  codeExplainContentDao.findAllByCodeNoOrderBySort(DictEnum.PROJECT_APPROVAL_STATE.name());

        for (Project g : content) {
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
            User u = AppUtil.getCurrentUser();
            if(codes.size()==0  || codes.get(codes.size()-1).getCodeName().equals(g.getApprovalStatus())){
                g.setIsApproval(false);
            }else{
                if (User.ADMIN.equals(user.getLevel()) ) { //超级管理员查看全部
                    g.setIsApproval(true);
                }else if ( User.ORG.equals(user.getLevel())){//省市管理员看对应的省市
                    if(user.getDeptId().equals(g.getApprovalDeptId())){
                        g.setIsApproval(true);
                    }else{
                        g.setIsApproval(false);
                    }
                }else{//普通用户
                    g.setIsApproval(false);
                }
            }

        }
        return pList;
    }

    @Override
    public void deleteProjectById(Long id) {
        projectDao.deleteById(id);
    }

    @Override
    public void deleteProjectsWithIds(List<Long> ids) {
        projectDao.deleteProjectsWithIds(ids);
    }

    @Override
    public List<Project> findAll() {
        //多基地orgIds
        return projectDao.findProjectsByOrgId(AppUtil.getCurrentUser().getOrgIdsData());
    }

    @Override
    public List<Project> findAllProjects() {
        //多基地orgIds
        return projectDao.findAllByOrgId(AppUtil.getCurrentUser().getOrgIdsData());
    }




    @Override
    public List<Project> findAllByOrgIds(List<Long> orgIds) {
        return projectDao.findAllByOrgId(orgIds);
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
        List<Project> projectList = projectDao.findByProjectNo(proejctNo, orgId);
        if (projectList.size() > 0) {
            throw new DataNotFoundException("编号已存在");
        }
    }



}
