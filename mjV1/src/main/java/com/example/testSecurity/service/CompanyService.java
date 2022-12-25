package com.example.testSecurity.service;

import com.example.testSecurity.dto.CompanyDto;
import org.json.simple.parser.ParseException;


public interface CompanyService {

    CompanyDto.Info createCompany(CompanyDto.Create companyCreateDTO) throws ParseException;

    CompanyDto.Info getCompany(Long no);

    CompanyDto.Info updateCompany(CompanyDto.Create companyCreateDTO, Long no);

    void deleteCompany(Long no);
}
