package com.smartpc.chiyun.dao.sys;

import com.smartpc.chiyun.model.sys.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
    @Query("select e from  Menu e order by e.sort asc")
    List<Menu> findAllMenuBySort();

    Menu findByTitle(String name);

    List<Menu> findByParentId(long pid);

    @Query(value = "select * from sys_menu where index_name in ?1  ", nativeQuery = true)
    List<Menu> findMenuIdByIndex(List<String> indexs);

    @Query(value = "select  * from sys_menu where id in ?1  order by sort asc  ", nativeQuery = true)
    List<Menu> findMenuByIds(List<Long> menuIds);

    @Query(value = "select * from  sys_menu e where  hidden = false and  e.app_id = ?1 order by e.sort asc", nativeQuery = true)
    List<Menu> findAllMenuByAppIdSort(Integer appId);

    Menu findByTitleAndAppId(String title, Integer appId);

    @Query(value = "select * from  sys_menu e where ((e.is_route = true and e.type =2 ) || e.type =1 || e.type =0) and  e.app_id = ?1 order by e.sort asc", nativeQuery = true)
    List<Menu> findAllRouteByAppIdSort(Integer appId);

    @Query(value = "select  id from sys_menu where hidden = true ", nativeQuery = true)
    List<Long> findMenuByHidden();

    @Query(value = "select  * from sys_menu where  app_id in (0,1) and  hidden = ?1 ", nativeQuery = true)
    List<Menu> findMenuByHidden(Boolean hidden);

    /**
     * 超级管理员初始加载
     *
     * @param flag
     * @return
     */
    @Query(value = "SELECT * FROM  sys_menu    " +
            "  WHERE  hidden = false and  flag = ?1  and  app_id in (0,1)" +
            "  order by sort ASC  ", nativeQuery = true)
    List<Menu> findMenuByHiddenAndFlag(Boolean flag);

    @Query(value = "SELECT * FROM  sys_menu    " +
            "  WHERE  hidden = false and  ((is_route = true and type =2 ) || type =1 || type =0) and  flag = ?1  " +
            "  order by sort ASC  ", nativeQuery = true)
    List<Menu> findMenuByHiddenAndFlagAndIsRoute(Boolean flag);
}
