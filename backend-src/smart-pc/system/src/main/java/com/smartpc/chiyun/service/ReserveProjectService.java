
package com.smartpc.chiyun.service;

import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.ReserveProject;
import com.smartpc.chiyun.model.query.ProjectQuery;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ReserveProjectService {
    /**
     * 通过id 查询项目
     *
     * @param id
     * @return
     */
    ReserveProject findReserveProjectById(Long id);



    /**
     * 分页查询 项目
     *
     * @param projectQuery
     * @return
     */
    public Page<ReserveProject> findAll(ProjectQuery projectQuery);

    void judgeProjectNoPresence(String proejctNo) throws DataNotFoundException;


    /**
     * 通过id删除项目数据
     *
     * @param id
     */
    @Transactional
    public void deleteReserveProjectById(Long id);

    /**
     * 查询所有projects（不分页）
     *
     * @return
     */
    List<ReserveProject> findAll();


}
