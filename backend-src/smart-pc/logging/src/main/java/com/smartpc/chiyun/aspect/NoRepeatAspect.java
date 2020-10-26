package com.smartpc.chiyun.aspect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartpc.chiyun.utils.*;
import com.smartpc.chiyun.aop.NoRepeat;
import com.smartpc.chiyun.domain.RepeatEntity;
import com.smartpc.chiyun.repository.RepeatRepository;
import com.smartpc.chiyun.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Aspect
public class NoRepeatAspect {

    @Autowired
    private RepeatRepository repeatRepository;

    @Pointcut("@within(com.smartpc.chiyun.aop.NoRepeat) || @annotation(com.smartpc.chiyun.aop.NoRepeat)")
    public void noRepeatCut() {

    }

    /**
     * 判断是否已经进行提交，没有就进行插入数据，有就进行判断并且直接返回
     */
    @Around("noRepeatCut() && @annotation(noRepeat)")
    public Object noRepeat(ProceedingJoinPoint point, NoRepeat noRepeat) throws Throwable {

        //获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        //获取IP地址
        String ipAddr = StringUtils.getIp(request);
        String url = request.getServletPath();
        RepeatEntity repeatEntity = new RepeatEntity();
        repeatEntity.setName(getUsername());
        //获取参数并转化为json字符串
        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = point.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)point.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                if(argValues[i] instanceof MultipartFile[]){
                    JSONArray jsonArray = JSONArray.parseArray(JSONArray.toJSONString(argValues[i]));
                    for(int j=0;j<jsonArray.size();j++){
                        JSONObject jUser = jsonArray.getJSONObject(j);
                        if(StringUtils.isNotBlank(jUser.get("originalFilename").toString())){
                            params.append(" ").append("originalFilename: ").append(jUser.get("originalFilename"));
                        }
                    }
                    //两种都可 下面一种应写成循环MultipartFile
//                    MultipartFile[] info = (MultipartFile[])argValues[i];
//                    String name = info[0].getOriginalFilename();
//                    System.out.println("name = " + name);
                }else{
                    params.append(" ").append(argNames[i]).append(": ").append(JSON.toJSONString(argValues[i]));
                }
            }
        }
        repeatEntity.setParams(params.toString() + " }");


        //这里没有将时间加入到MD5中是为了防止出现跨分钟 跨小时的情况
        String dataStr = FileUtil.getMessageDigest(ipAddr + url + repeatEntity.getName() +
                repeatEntity.getParams());
        repeatEntity.setRequestIp(ipAddr);
        repeatEntity.setMdString(url);

        repeatEntity.setCreateTime(new Timestamp(new Date().getTime()));
        repeatEntity.setMdString(dataStr);


        Long baseTime = Long.parseLong(noRepeat.value().getValue());
        String cacheName = noRepeat.value().getPath();
        //判断单位时间内是否存在相同的记录
        EhcacheUtil ehcacheUtil = EhcacheUtil.getInstance();
        ehcacheUtil.createCacheByTime(cacheName,baseTime);

        Object obj = ehcacheUtil.get(cacheName,"repeatInfo");

        //字符串转map
        List<String> ts = (List<String>)JSONObject.parseArray(JSON.toJSONString(obj),String.class);
        if(ts ==null ){
            ts = new ArrayList<>();
        }
        if(ts.contains(dataStr)){
            return  new ResultVO("fail","请不要短时间重复提交");
        }else{
            ts.add(dataStr);
            ehcacheUtil.put(cacheName,"repeatInfo",ts);
        }

        return point.proceed();
    }


    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        }catch (Exception e){
            return "";
        }
    }

}
