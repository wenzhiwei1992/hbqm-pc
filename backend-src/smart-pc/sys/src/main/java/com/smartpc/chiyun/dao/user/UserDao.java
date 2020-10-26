
package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findByIdAndOrgId(Long id,Long orgId);

    @Query(value = "SELECT * FROM pc_user WHERE id = ?1 ",nativeQuery = true)
    User findByUserId(Long id);
    @Modifying
    @Query("delete from User u where u.id in ?1")
    void deletes(List<Long> ids);

    List<User> findByIdIn(List<Long> ids);
    @Query(value = "SELECT * FROM pc_user WHERE `password` = ?1 ",nativeQuery = true)
    User findByPassword(String password);

    @Query("select e from User e where e.password = ?1 and e.username = ?2 ")
    User findByPasswordAndUserName(String pwd ,String username);

    @Query("select e from User e where e.orgId = ?1  ")
    List<User> findUsersByOrgId(Long orgId);


    @Query("select e from User e where e.orgId = ?1 and e.username = ?2 ")
    List<User> findUsersByOrgId(Long orgId,String username);

    @Query("select e from User e where  e.username = ?1 ")
    List<User> findByUsernameList(String username);
}
