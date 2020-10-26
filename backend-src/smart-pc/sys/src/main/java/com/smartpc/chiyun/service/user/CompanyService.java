package com.smartpc.chiyun.service.user;

import com.smartpc.chiyun.controller.user.dto.CompanyVO;
import com.smartpc.chiyun.model.user.Company;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyService {
    Company getCompanyById(Long id);
    Page<Company> list(CompanyVO companyVO);

    @Transactional
    public void deleteCompanyById(Long id);

    @Transactional
    public void deleteCompanysWithIds(List<Long> ids);
    List<Company> getCompanies();
}
