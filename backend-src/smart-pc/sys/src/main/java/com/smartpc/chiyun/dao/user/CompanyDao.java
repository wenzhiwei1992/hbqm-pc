package com.smartpc.chiyun.dao.user;



import com.smartpc.chiyun.model.user.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CompanyDao extends JpaRepository<Company, Long> {
    @Query(value = "SELECT * FROM pc_company WHERE id = ?1 ",nativeQuery = true)
    Company  findCompanyById(Long id);
    List<Company> findAll(Specification<Company> spc, Pageable pageable);
    @Modifying
    @Query("delete from Company c where c.id in ?1")
    void deleteCompanysWithIds(List<Long> ids);
    @Query(value = "SELECT * FROM pc_company ",nativeQuery = true)
    List<Company> findCompanies();


}
