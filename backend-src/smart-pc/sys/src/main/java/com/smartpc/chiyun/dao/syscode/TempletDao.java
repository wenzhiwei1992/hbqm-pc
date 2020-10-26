package com.smartpc.chiyun.dao.syscode;

import com.smartpc.chiyun.model.syscode.Templet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author yue
 * @create 2020/2/17 4:27 下午
 */
@Repository
public interface TempletDao extends JpaRepository<Templet, Long>, JpaSpecificationExecutor<Templet> {

    @Modifying
    @Query("delete from Templet t where t.id in ?1")
    void deleteTempletsWithIds(List<Long> ids);


    Page<Templet> findAll(Specification<Templet> spc, Pageable pageable);

    @Query(value = "select  * from ma_templet where id = ?1 ", nativeQuery = true)
    Templet findTempletById(Long templetId);

    @Query(value = "select  * from ma_templet where templet_no = ?1", nativeQuery = true)
    List<Templet> findTempletByTempletNo(String templetNo);

}
