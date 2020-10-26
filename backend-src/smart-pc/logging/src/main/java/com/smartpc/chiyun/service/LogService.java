package com.smartpc.chiyun.service;

import com.smartpc.chiyun.domain.Log;
import com.smartpc.chiyun.service.dto.LogSmallDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LogService {


    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log);


    void download(List<Log> queryAll, HttpServletResponse response) throws IOException;

    Page<Log> findAll(LogSmallDTO logSmallDTO);

    void saveManual(String desc,String username, String browser, String ip, Log log);
}
