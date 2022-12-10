package com.example.testSecurity.service;

import com.example.testSecurity.dto.CompanyDto;

public interface CompanyService {
    void createCompany(CompanyDto.Create companyCreateDTO);

    void getCompany(Long no);

    void updateCompany(CompanyDto.Create companyCreateDTO, Long no);

    void deleteCompany(Long no);
}
