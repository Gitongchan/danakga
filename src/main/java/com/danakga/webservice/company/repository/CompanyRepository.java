package com.danakga.webservice.company.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyInfo,Integer> {

    Optional<CompanyInfo> findByCompanyName(String companyName);

}
