package com.smartpc.chiyun.controller.quartz;

import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.exception.DataNotFoundException;
import com.smartpc.chiyun.model.quartz.QuartzJob;

import com.smartpc.chiyun.service.quartz.QuartzJobService;
import com.smartpc.chiyun.service.quartz.dto.JobQueryCriteria;
import com.smartpc.chiyun.vo.ManualPageVo;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


/**
 * @Author yue
 * @create 2020/8/14 10:55 上午
 */
@RestController
@RequestMapping("/quartzJob")
public class QuartzJobController {
    @Autowired
    QuartzJobService quartzJobService;

    @Log("分页查询定时任务")
    @ApiOperation("分页查询定时任务")
    @PostMapping("/findAllQuartzJob")
    public ResultVO findAllQuartzJob(@RequestBody JobQueryCriteria criteria) {
        ManualPageVo<QuartzJob> list = quartzJobService.queryAllLog(criteria);
        return ResultVOUtils.success(list);
    }

    @Log("新增定时任务")
    @ApiOperation("新增定时任务")
    @PostMapping("/addQuartzJob")
    public ResultVO addQuartzJob(@RequestBody QuartzJob resources) {
        try {
            quartzJobService.addQuartzJob(resources);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success();
    }

    @Log("更新定时任务")
    @ApiOperation("更新定时任务")
    @PostMapping("/updateQuartzJob")
    public ResultVO updateQuartzJob(@RequestBody QuartzJob resources) {
        try {
            quartzJobService.updateQuartzJob(resources);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success();
    }

    @Log("更改定时任务状态")
    @ApiOperation("更改定时任务状态")
    @GetMapping("/updateIsPause/{id}")
    public ResultVO updateIsPause(@PathVariable Long id) {
        try {
            quartzJobService.updateIsPause(id);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success();
    }

    @Log("通过ID查询任务")
    @ApiOperation("通过ID查询任务")
    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable Long id) {
        QuartzJob quartzJob = quartzJobService.findById(id);
        return ResultVOUtils.success(quartzJob);
    }

    @Log("删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping("/deleteQuartzJob")
    public ResultVO deleteQuartzJob(String ids) {
        quartzJobService.deleteQuartzJob(ids);
        return ResultVOUtils.success();
    }


    @Log("更改定时任务时间")
    @ApiOperation("更改定时任务时间")
    @PostMapping("/updateJobTime")
    public ResultVO updateQuartzJobTime(@RequestBody QuartzJob quartzJob) {
        try {
            quartzJobService.updateQuartzJobTime(quartzJob);
        } catch (DataNotFoundException ex) {
            return ResultVOUtils.error(ResultEnum.PAGEINFODATAERR.getStatus(), ex.getMsg());
        }
        return ResultVOUtils.success();
    }

}
