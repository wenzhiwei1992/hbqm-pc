
package com.smartpc.chiyun.controller;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ProjectDao;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.query.ProjectQuery;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.service.ProjectService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    ProjectDao projectDao;
    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ResultVO getProjectById(@PathVariable Long id) {
        Project project = projectService.findProjectById(id);
        return ResultVOUtils.success(project);
    }

    @Log("删除项目")
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public SR<Project> delete(@PathVariable Long id) {
        SR sr = new SR();
        try {
            projectService.deleteProjectById(id);
        } catch (Exception e) {
            sr.setMsg("该信息已有关联信息,不能删除！");
            return sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("删除项目")
    @RequestMapping(value = "/project/projects", method = RequestMethod.DELETE)
    public SR<Project> delete(String ids) {
        String[] split = ids.split(";");
        List<Long> list = new ArrayList<>();
        for (String id : split) {
            list.add(Long.parseLong(id));
        }
        SR sr = new SR();
        try {
            projectService.deleteProjectsWithIds(list);
        } catch (Exception e) {
            sr.setMsg("已选信息中已有其他关联信息,不能删除！");
            return sr;
        }
        sr.setStatus(SR.SUCCESS);
        return sr;
    }



    /**
     * 获取项目列表(不分页)  //多基地orgIds
     *
     * @return
     */
    @RequestMapping(value = "/project/listByAuthority", method = RequestMethod.GET)
    public SR<List<Project>> listAll() {
        List<Project> projects = projectService.findAll();
        SR<List<Project>> sr = new SR<>();
        sr.setEntity(projects);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }


    @GetMapping("/project/findAllByOrgIds")
    public ResultVO findAllByOrgIds() {
        List<Long> orgIdsList = AppUtil.getCurrentUserOrgIds();
        List<Project> list = projectService.findAllByOrgIds(orgIdsList);
        return ResultVOUtils.success(list);
    }


    /**
     * 获取组织下的项目信息
     *
     * @param projectQuery
     * @return
     */
    @Log("查询项目")
    @RequestMapping(value = "/project/list", method = RequestMethod.POST)
    public SR<Page<Project>> list(@RequestBody ProjectQuery projectQuery, HttpServletRequest request) {
        SR<Page<Project>> sr = new SR<>();
        Page<Project> pageableList = projectService.findAll(projectQuery);
        sr.setEntity(pageableList);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @Log("更新项目")
    @RequestMapping(value = "/project/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody Project project) {
        if (StringUtils.isEmpty(project.getProjectName())) {
            return ResultVOUtils.error(SR.FAIL, "项目名称不能为空");
        }

        if (StringUtils.isEmpty(project.getWorkPlace())) {
            return ResultVOUtils.error(SR.FAIL, "工地名称不能为空");
        }
        if (StringUtils.isEmpty( project.getCustomer())) {
            return ResultVOUtils.error(SR.FAIL, "客户名称不能为空");
        }
        Project query = projectDao.findProjectById(project.getId());
        if (!query.getProjectNo().equals(project.getProjectNo())) {
            try {
                projectService.judgeProjectNoPresence(project.getProjectNo());
            } catch (DataNotFoundException e) {
                return ResultVOUtils.error(SR.FAIL, e.getMsg());
            }
        }

        Project saveAndFlush = projectDao.saveAndFlush(project);
        return ResultVOUtils.success(saveAndFlush);
    }

    @Log("新增项目")
    @RequestMapping(value = "/project/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody Project project) {
        SR<Project> sr = new SR<>();
        try {
            projectService.judgeProjectNoPresence(project.getProjectNo());
        } catch (DataNotFoundException e) {
            return ResultVOUtils.error(SR.FAIL, e.getMsg());
        }
        Project save = projectDao.save(project);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return ResultVOUtils.success(save);

    }




}
