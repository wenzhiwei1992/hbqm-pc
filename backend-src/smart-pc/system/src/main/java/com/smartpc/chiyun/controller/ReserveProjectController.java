
package com.smartpc.chiyun.controller;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.ProjectDao;
import com.smartpc.chiyun.dao.ReserveProjectDao;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.ReserveProject;
import com.smartpc.chiyun.model.query.ProjectQuery;
import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.service.ProjectService;
import com.smartpc.chiyun.service.ReserveProjectService;
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
@RequestMapping("/reserveproject")
public class ReserveProjectController {
    @Autowired
    ReserveProjectDao reserveProjectDao;
    @Autowired
    ReserveProjectService peserveProjectService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultVO getProjectById(@PathVariable Long id) {
        ReserveProject project = peserveProjectService.findReserveProjectById(id);
        return ResultVOUtils.success(project);
    }

    @Log("删除储备项目")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultVO delete(@PathVariable Long id) {
        try {
            peserveProjectService.deleteReserveProjectById(id);
        } catch (Exception e) {
            ResultVOUtils.error("fail","该信息已有关联信息,不能删除！");
        }
        return ResultVOUtils.success();
    }




    /**
     * 获取项目列表(不分页)  //多基地orgIds
     *
     * @return
     */
    @RequestMapping(value = "/listByAuthority", method = RequestMethod.GET)
    public ResultVO listAll() {
        List<ReserveProject> projects = peserveProjectService.findAll();
        return ResultVOUtils.success(projects);
    }


    @GetMapping("/findAllByOrgIds")
    public ResultVO findAllByOrgIds() {
        List<ReserveProject> list = peserveProjectService.findAll();
        return ResultVOUtils.success(list);
    }


    /**
     * 获取组织下的项目信息
     *
     * @param projectQuery
     * @return
     */
    @Log("查询项目")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultVO list(@RequestBody ProjectQuery projectQuery, HttpServletRequest request) {
        Page<ReserveProject> pageableList = peserveProjectService.findAll(projectQuery);
        return ResultVOUtils.success(pageableList);
    }

    @Log("更新项目")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultVO update(@RequestBody ReserveProject project) {
        ReserveProject query = reserveProjectDao.findReserveProjectById(project.getId());
        if (!query.getProjectNo().equals(project.getProjectNo())) {
            try {
                peserveProjectService.judgeProjectNoPresence(project.getProjectNo());
            } catch (DataNotFoundException e) {
                return ResultVOUtils.error(SR.FAIL, e.getMsg());
            }
        }

        ReserveProject saveAndFlush = reserveProjectDao.saveAndFlush(project);
        return ResultVOUtils.success(saveAndFlush);
    }

    @Log("新增项目")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO add(@RequestBody ReserveProject project) {
        try {
            peserveProjectService.judgeProjectNoPresence(project.getProjectNo());
        } catch (DataNotFoundException e) {
            return ResultVOUtils.error(SR.FAIL, e.getMsg());
        }
        ReserveProject save = reserveProjectDao.save(project);
        return ResultVOUtils.success(save);

    }


}
