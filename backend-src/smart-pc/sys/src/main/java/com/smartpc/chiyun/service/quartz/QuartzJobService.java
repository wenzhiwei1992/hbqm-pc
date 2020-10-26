package com.smartpc.chiyun.service.quartz;

import com.smartpc.chiyun.model.quartz.QuartzJob;
import com.smartpc.chiyun.model.quartz.QuartzLog;
import com.smartpc.chiyun.service.quartz.dto.JobQueryCriteria;
import com.smartpc.chiyun.vo.ManualPageVo;

import java.util.List;
import java.util.Set;

/**
 * @Author yue
 * @create 2020/8/13 4:10 下午
 */
public interface QuartzJobService {
    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    ManualPageVo<QuartzJob> queryAllLog(JobQueryCriteria criteria);

    /**
     * 创建
     *
     * @param resources /
     */
    void addQuartzJob(QuartzJob resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateQuartzJob(QuartzJob resources);

    /**
     * 删除任务
     *
     * @param ids /
     */
    void deleteQuartzJob(String ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    QuartzJob findById(Long id);

    /**
     * 查询所有启动的任务
     *
     * @return
     */
    List<QuartzJob> findByIsPauseIsTrue();

    /**
     * 更改定时任务状态
     *
     * @param id /
     */
    void updateIsPause(Long id);

    /**
     * 根据jobname修改定时时间
     *
     */
    void updateQuartzJobTime(QuartzJob resources);
}
