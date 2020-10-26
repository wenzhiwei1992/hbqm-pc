package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.UserM2EpWx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserM2EpWxDao extends JpaRepository<UserM2EpWx,Integer> {

    @Query("select e from UserM2EpWx e where e.epWxUserId = ?1 and e.type = ?2")
    List<UserM2EpWx> findByEpUserId(String qywxUserId,Integer type);

    @Query("select e from UserM2EpWx e where e.userId = ?1 and e.type = ?2")
    UserM2EpWx findByUserIdAndType(String userId,Integer type);

    @Query("select e from UserM2EpWx e where (e.userId = ?1 or e.epWxUserId = ?2) and e.type = ?3 ")
    List<UserM2EpWx> findByUserIdAndEpWxUserId(String userId,String epWxUserId,Integer type);

    @Query("select epWxUserId from UserM2EpWx e where e.userId in ?1 and e.type = ?2")
    List<String> findByUserIdsAndType(String[] userIds,int type);


    @Query("select epWxUserId from UserM2EpWx e where e.userId in (" +
            "select userId from UserGroup where groupId = ?1 " +
            ") and e.type = ?2")
    List<String> findByTypeAndGroupIds(String[] groupIds,int type);
}
