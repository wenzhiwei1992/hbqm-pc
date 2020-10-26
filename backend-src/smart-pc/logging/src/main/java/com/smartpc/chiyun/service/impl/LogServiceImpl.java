package com.smartpc.chiyun.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.smartpc.chiyun.aop.Log;
import com.smartpc.chiyun.repository.LogRepository;
import com.smartpc.chiyun.service.LogService;
import com.smartpc.chiyun.service.dto.LogSmallDTO;
import com.smartpc.chiyun.utils.FileUtil;
import com.smartpc.chiyun.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, com.smartpc.chiyun.domain.Log log){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        assert log != null;
        log.setRequestIp(ip);

        String LOGINPATH = "login";
        if(LOGINPATH.equals(signature.getName())){
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        logRepository.save(log);
    }


    @Override
    public void download(List<com.smartpc.chiyun.domain.Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (com.smartpc.chiyun.domain.Log log : logs) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", log.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Page<com.smartpc.chiyun.domain.Log> findAll(LogSmallDTO logSmallDTO) {
        com.smartpc.chiyun.domain.Log log  = new com.smartpc.chiyun.domain.Log();
        BeanUtils.copyProperties(logSmallDTO,log);
        Pageable pageable= PageRequest.of(logSmallDTO.getPageInfo().getCurrentPage()-1,logSmallDTO.getPageInfo().getPageSize(),new Sort(Sort.Direction.DESC, "id"));
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", match -> match.contains());//contains ==> like %{xxx}%
        Example<com.smartpc.chiyun.domain.Log> example = Example.of(log, matcher);
        Page<com.smartpc.chiyun.domain.Log> logs = logRepository.findAll(example,pageable);
        return logs;
    }

    @Override
    public void saveManual(String desc,String username, String browser, String ip, com.smartpc.chiyun.domain.Log log) {
        if (log != null) {
            log.setDescription(desc);
        }
        assert log != null;
        log.setRequestIp(ip);

        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod("");
        log.setUsername(username);
        log.setParams("");
        log.setBrowser(browser);
        logRepository.save(log);
    }
}
