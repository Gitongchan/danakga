package com.danakga.webservice.inquiry.repository;

import com.danakga.webservice.inquiry.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
