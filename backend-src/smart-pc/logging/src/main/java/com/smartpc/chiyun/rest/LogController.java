package com.smartpc.chiyun.rest;

import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.domain.Log;
import com.smartpc.chiyun.service.LogService;
import com.smartpc.chiyun.service.dto.LogSmallDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public JSONObject getLogs(@RequestBody LogSmallDTO logSmallDTO){
        Page<Log> page =  logService.findAll(logSmallDTO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",page);
        jsonObject.put("status","success");
        return jsonObject;
    }

}
