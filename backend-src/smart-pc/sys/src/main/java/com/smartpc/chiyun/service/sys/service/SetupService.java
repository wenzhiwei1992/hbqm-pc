package com.smartpc.chiyun.service.sys.service;

import com.smartpc.chiyun.dao.sys.SetupDao;
import com.smartpc.chiyun.model.sys.Setup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SetupService {
    @Autowired
    SetupDao dao;

    public Setup load(){
        List<Setup> all = dao.findAll();
        if(all==null||all.size()==0){
            return null;
        }
        return all.get(0);
    }

}
