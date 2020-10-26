package com.smartpc.chiyun.service.user.impl;

import com.smartpc.chiyun.controller.user.dto.CompanyVO;
import com.smartpc.chiyun.dao.user.CompanyDao;
import com.smartpc.chiyun.model.user.Company;
import com.smartpc.chiyun.service.user.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyDao companyDao;

    @Override
    public Company getCompanyById(Long id) {
        return companyDao.findCompanyById(id);
    }

    @Override
    public Page<Company> list(CompanyVO companyVO) {
        Company company = new Company();
        BeanUtils.copyProperties(companyVO, company);
        Pageable pageable = PageRequest.of(companyVO.getPageInfo().getCurrentPage() - 1, companyVO.getPageInfo().getPageSize(), new Sort(Sort.Direction.DESC, "dateModified"));
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("companyName", match -> match.contains());//contains ==> like %{xxx}%
        Example<Company> example = Example.of(company, matcher);
        return companyDao.findAll(example, pageable);
    }

    @Override
    public void deleteCompanyById(Long id) {
        companyDao.deleteById(id);
    }

    @Override
    public void deleteCompanysWithIds(List<Long> ids) {
        companyDao.deleteCompanysWithIds(ids);
    }

    @Override
    public List<Company> getCompanies() {
        return companyDao.findCompanies();
    }
}
