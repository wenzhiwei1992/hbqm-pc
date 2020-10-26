package com.smartpc.chiyun.dao.user;

import com.smartpc.chiyun.model.user.GroupMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;

/**
 * @Author yue
 * @create 2020/3/12 3:14 下午
 */
public interface GroupMenuDao extends JpaRepository<GroupMenu, Long>, JpaSpecificationExecutor<GroupMenu> {
    /**
     * 解绑角色菜单
     *
     * @param id
     */
    @Modifying
    @Query(value = "delete from pc_group_menu where menu_id = ?1", nativeQuery = true)
    void untiedMenu(Long id);

    List<GroupMenu> findGroupMenusByGroupId(Long groupId);

    @Query(value = "SELECT m.id FROM pc_group_menu gm " +
            "  INNER JOIN pc_group g ON gm.group_id = g.id " +
            "  INNER JOIN sys_menu m ON gm.menu_id = m.id " +
            "  WHERE  hidden = false and g.id in ?1 and  m.flag = ?2  " +
            "  order by m.sort ASC  ", nativeQuery = true)
//Integer appId, and m.app_id = ?2
    List<Long> findByGroupIdsAndSortAsc(Set<Long> groupIds, Boolean flag);

    @Modifying
    @Query(value = "delete from  pc_group_menu gm  where gm.group_id  in ?1  ", nativeQuery = true)
    void deleteGroupMenuByIds(List<Long> groupIds);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM pc_group_menu WHERE group_id = ?1  ", nativeQuery = true)
    void deleteGroupMenuByGroupId(Long groupId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM pc_group_menu WHERE  menu_id in ?1  and  group_id = ?2  ", nativeQuery = true)
    void deleteGroupMenuByMenuIdsAndGroupId(Set<Long> menuIds, Long groupId);

    @Query(value = "SELECT m.id FROM pc_group_menu gm " +
            "  INNER JOIN pc_group g ON gm.group_id = g.id " +
            "  INNER JOIN sys_menu m ON gm.menu_id = m.id " +
            "  WHERE  ((m.is_route = true and m.type =2 ) || m.type =1 || m.type =0)    and  g.id in ?1   and  m.flag = ?2  " +
            "   ", nativeQuery = true)
//and m.app_id = ?2   Integer appId,
    List<Long> findRouteByGroupIdsAndSortAsc(Set<Long> groupIds, Boolean flag);

    @Query(value = "SELECT m.id FROM pc_group_menu gm " +
            "  INNER JOIN pc_group g ON gm.group_id = g.id " +
            "  INNER JOIN sys_menu m ON gm.menu_id = m.id " +
            "  WHERE  hidden = false and g.id in ?1 and m.app_id = ?2  " +
            "  order by m.sort ASC  ", nativeQuery = true)
    List<Long> findByGroupIdsAndSortAsc(Set<Long> groupIds, Integer appId);



}
