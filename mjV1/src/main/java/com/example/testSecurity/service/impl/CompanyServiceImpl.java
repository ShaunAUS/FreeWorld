package com.example.testSecurity.service.impl;

import com.example.testSecurity.dto.CompanyDto;
import com.example.testSecurity.entity.Company;
import com.example.testSecurity.exception.ServiceProcessException;
import com.example.testSecurity.exception.enums.ServiceMessage;
import com.example.testSecurity.repository.CompanyJpaRepository;
import com.example.testSecurity.service.CompanyService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.transaction.Transactional;
import java.net.HttpURLConnection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyJpaRepository companyJpaRepository;

    @Override
    public CompanyDto.Info createCompany(CompanyDto.Create companyCreateDTO) throws ParseException {
        //사업자 조회 open API
        checkBusinessNumber(companyCreateDTO.getBusinessNumber());
        Company savedCompany = companyJpaRepository.save(CompanyDto.toEntity(companyCreateDTO));

        return CompanyDto.toDto(savedCompany);
    }

    private void checkBusinessNumber(Integer number) throws ParseException {
        StringBuffer result = new StringBuffer();
        try {
            StringBuilder urlBuilder = new StringBuilder(
                "http://api.odcloud.kr/api/nts-businessman/v1/status?");
            urlBuilder.append(
                "serviceKey="
                    + "f1YWtB49b%2FzZmef6OUiFHg%2BZi4TMSJqHaYx8BNuajoctGpZ9luYfwWixyXDLb1wAvovSUUSN4pFP8plGyVhVgQ%3D%3D");
            urlBuilder.append("&returnType=JSON"); // 호출문서 형태

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json");  // 받기
            conn.setDoOutput(true); // 받아온 Json 출력 가능상태로

            log.info("Request URL = {}", url);

            String businessNumber = "{  \"b_no\": [    \"" + number + "\"  ]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = businessNumber.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            rd.close();
            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }

        //파싱
        JSONObject ojbData = (JSONObject) new JSONParser().parse(String.valueOf(result));

        //Json 배열데이터 열기
        JSONArray arrData = (JSONArray) ojbData.get("data");

        // 배열 데이터 출력하기
        JSONObject tmp = null;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arrData.size(); i++) {

            //data
            tmp = (JSONObject) arrData.get(i);

            sb.append("사업자번호 : " + tmp.get("b_no") + "\n");
            sb.append("과세유형메세지(명칭) : " + tmp.get("tax_type") + "\n");
        }

        log.info(sb.toString());

        if (tmp.get("tax_type").equals("국세청에 등록되지 않은 사업자등록번호입니다.")) {
            throw new ServiceProcessException(ServiceMessage.COMPANY_NOT_FOUND);
        }
    }

    @Override
    public CompanyDto.Info getCompany(Long companyNo) {

        Optional<Company> companyById = companyJpaRepository.findById(companyNo);

        if (companyById.isPresent()) {
            return CompanyDto.toDto(companyById.get());
        } else {
            throw new ServiceProcessException(ServiceMessage.COMPANY_NOT_FOUND);
        }
    }

    @Override
    public CompanyDto.Info updateCompany(CompanyDto.Create companyCreateDTO, Long companyNo) {

        Optional<Company> companyById = companyJpaRepository.findById(companyNo);
        if (companyById.isPresent()) {
            Company.changeCompany(companyCreateDTO, companyById.get());
        }
        return CompanyDto.toDto(companyById.get());
    }

    @Override
    public void deleteCompany(Long companyNo) {
        companyJpaRepository.deleteById(companyNo);
    }
}

