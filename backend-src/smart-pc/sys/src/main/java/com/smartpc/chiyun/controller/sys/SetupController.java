package com.smartpc.chiyun.controller.sys;

import com.smartpc.chiyun.config.AppUtil;
import com.smartpc.chiyun.dao.sys.SetupDao;
import com.smartpc.chiyun.enums.ResultEnum;
import com.smartpc.chiyun.service.sys.service.SetupService;
import com.smartpc.chiyun.vo.ResultVO;
import com.smartpc.chiyun.voutils.ResultVOUtils;

import com.smartpc.chiyun.model.sys.SR;
import com.smartpc.chiyun.model.sys.Setup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SetupController {
    @Autowired
    SetupDao d;
    @Autowired
    SetupService s;

    @RequestMapping(value = "/setup", method = RequestMethod.GET)
    public SR<Setup> load() {
        SR<Setup> sr=new SR<>();
        sr.setEntity(s.load());
        sr.setStatus(SR.SUCCESS);
        return sr;
    }
    @RequestMapping(value = "/setup/update", method = RequestMethod.POST)
    public SR<Setup> update(@RequestBody  Setup entity) {
        SR<Setup> sr=new SR<>();
        Setup query = d.findById(entity.getId()).get();
        if(query==null){
            sr.setMsg("设置不存在");
            return sr;
        }

        Setup save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/setup/add", method = RequestMethod.POST)
    public SR<Setup> add(@RequestBody Setup entity) {
        SR<Setup> sr=new SR<>();

        Setup save = d.save(entity);
        sr.setEntity(save);
        sr.setStatus(SR.SUCCESS);
        return sr;
    }

    @RequestMapping(value = "/setup/loadPcSetup", method = RequestMethod.GET)
    public ResultVO loadPcSetup() {
        List<Setup> byOrgId = d.findByOrgId(AppUtil.getCurrentUserOrgId());
        if (byOrgId.size()>0)
        {
            return ResultVOUtils.success(byOrgId.get(0));
        }
        return ResultVOUtils.error(ResultEnum.DATASIZEISZORE);
    }
}
