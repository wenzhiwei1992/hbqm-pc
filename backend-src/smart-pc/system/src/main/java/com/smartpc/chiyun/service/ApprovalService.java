package com.smartpc.chiyun.service;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ProjectDao;
import com.smartpc.chiyun.dao.ReserveProjectDao;
import com.smartpc.chiyun.dao.syscode.CodeExplainContentDao;
import com.smartpc.chiyun.dao.ApprovalDao;
import com.smartpc.chiyun.dao.user.OrgDao;
import com.smartpc.chiyun.enums.DictEnum;
import com.smartpc.chiyun.model.Approval;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.ReserveProject;
import com.smartpc.chiyun.model.user.Org;
import com.smartpc.chiyun.model.user.vo.ApprovalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ApprovalService {
    @Autowired
    ApprovalDao approvalDao;

    @Autowired
    AppUtil appUtil;

    @Autowired
    CodeExplainContentDao dictDao;


    @Autowired
    OrgDao orgDao;

    @Autowired
    ProjectDao projectDao;

    @Autowired
    ReserveProjectDao reserveProjectDao;


    @PersistenceContext
    EntityManager entityManager;


    @Transactional
    public String saveApprovalByFirst(Approval detail) {
        detail.setApprovalLevel("提交审批");
        detail.setApprovalerId(AppUtil.getCurrentUser().getId());
        detail.setApprovalDeptId(detail.getProvinceId());

        approvalDao.save(detail);
        return "success";
    }


    @Transactional
    public String saveApproval(Approval detail) {
        String nextStatus = "";
        detail.setApprovalLevel(detail.getNowCodeName());
        detail.setApprovalerId(AppUtil.getCurrentUser().getId());
        if("合格".equals(detail.getApprovalStatus())){
            nextStatus = appUtil.getNextStatus(detail.getNowCodeName(), detail.getCodeType());
            detail.setApprovalDeptId(detail.getProvinceId());
        }else{
            detail.setApprovalDeptId(detail.getCityId());
            nextStatus = appUtil.getPreviousStatus(detail.getNowCodeName(), detail.getCodeType());
        }
        if(DictEnum.PROJECT_APPROVAL_STATE.name().equals(detail.getCodeType())){
            Project project = projectDao.findProjectById(detail.getAssociationId());
            if(!project.getApprovalStatus().equals(detail.getNowCodeName())){//状态位不匹配
                return "所审批数据状态位已发生变更，请确认后重新审批";
            }
            project.setApprovalStatus(nextStatus);
            project.setApprovalDeptId(detail.getApprovalDeptId());
            projectDao.save(project);
        }else if(DictEnum.ORG_APPROVAL_STATE.name().equals(detail.getCodeType())){
            Org org = orgDao.findOrgById(detail.getAssociationId());
            if(!org.getApprovalStatus().equals(detail.getNowCodeName())){//状态位不匹配
                return "所审批数据状态位已发生变更，请确认后重新审批";
            }
            org.setApprovalStatus(nextStatus);
            org.setApprovalDeptId(detail.getApprovalDeptId());
            orgDao.save(org);
        }else if(DictEnum.RESERVE_PROJECT_APPROVAL_STATE.name().equals(detail.getCodeType())){
            ReserveProject project = reserveProjectDao.findReserveProjectById(detail.getAssociationId());
            if(!project.getApprovalStatus().equals(detail.getNowCodeName())){//状态位不匹配
                return "所审批数据状态位已发生变更，请确认后重新审批";
            }
            project.setApprovalStatus(nextStatus);
            project.setApprovalDeptId(detail.getApprovalDeptId());
            reserveProjectDao.save(project);
        }


        approvalDao.save(detail);
        return "success";
    }

    /**
     * 查询组织根据当前登录用户；超级管理员查询所有组织，组织管理员查询当前组织
     *
     * @return
     */
    public List<Approval> findOrgApprovalByOrgId(Approval detail) {
        try{
            List<Approval> approvals =  approvalDao.findByAssociationIdAndCodeType(detail.getAssociationId(),detail.getCodeType());
          //  return approvalDao.findByOrgId(orgId);
        }catch (Exception e){
            log.error("查询基地审批信息失败：detail："+ JSONObject.toJSONString(detail),e);
        }
        return new ArrayList<>();
    }

    public List<ApprovalVO> getApprovalVOInfo(Approval vo) {

    String sql =
            "SELECT "
            + " t1.dateCreated, "
            + " t1.approval_level, "
            + " t1.approval_status, "
            + " t1.reason, "
            + " t2.username,t3.dept_name  "
            + "FROM "
            + " `pc_approval` t1 "
            + " INNER JOIN pc_user t2 ON t1.approvaler_id = t2.id "
            + " INNER JOIN ma_department t3 ON t3.id = t1.approval_dept_id  ";

        sql += "where t1.code_type =:code_type " ;
        sql += " and t1.association_id = :association_id ";
        Query nativeQuery = entityManager.createNativeQuery(sql);

        nativeQuery.setParameter("code_type", vo.getCodeType());
        nativeQuery.setParameter("association_id", vo.getAssociationId());

        List<Object[]> objectList = nativeQuery.getResultList();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<ApprovalVO> list = new ArrayList<>();
        for (int i = 0; i < objectList.size(); i++) {
            ApprovalVO data = new ApprovalVO();
            Object[] objects = objectList.get(i);
            Date dateCreated = (Date)(objects[0]);
            String approval_level = String.valueOf(objects[1] == null ? "" : objects[1]);
            String approval_status = String.valueOf(objects[2] == null ? "" : objects[2]);
            String reason = String.valueOf(objects[3] == null ? "" : objects[3]);
            String username = String.valueOf(objects[4] == null ? "" : objects[4]);
            String dept_name = String.valueOf(objects[5] == null ? "" : objects[5]);


            data.setDateCreated(format.format(dateCreated));
            data.setApprovalStatus(approval_status);
            data.setApprovalLevel(approval_level);
            data.setReason(reason);
            data.setApprovalerName(username);
            data.setDeptName(dept_name);

            list.add(data);
        }

        return list;
    }

}
