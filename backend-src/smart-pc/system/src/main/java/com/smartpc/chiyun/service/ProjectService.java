
package com.smartpc.chiyun.service;

import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.Project;
import com.smartpc.chiyun.model.query.ProjectQuery;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ProjectService {
    /**
     * 通过id 查询项目
     *
     * @param id
     * @return
     */
    Project findProjectById(Long id);



    /**
     * 分页查询 项目
     *
     * @param projectQuery
     * @return
     */
    public Page<Project> findAll(ProjectQuery projectQuery);

    /**
     * 通过id删除项目数据
     *
     * @param id
     */
    @Transactional
    public void deleteProjectById(Long id);

    /**
     * 通过ids批量删除项目数据
     *
     * @param ids
     */
    @Transactional
    public void deleteProjectsWithIds(List<Long> ids);

    /**
     * 查询所有projects（不分页）
     *
     * @return
     */
    List<Project> findAll();

    /**
     * 查询所有projects（不分页）
     *
     * @return
     */
    List<Project> findAllProjects();


    List<Project> findAllByOrgIds(List<Long> orgIds);

    void judgeProjectNoPresence(String proejctNo) throws DataNotFoundException;


}
