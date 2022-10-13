package com.danakga.webservice.siteinquiry.repository;

import com.danakga.webservice.siteinquiry.model.SiteInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SiteInquiryRepository extends JpaRepository<SiteInquiry, Long> {

    /* 문의 사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update SiteInquiry si set si.sinDeleted = 'Y' where si.sinId = :inId")
    void updateInDeleted(@Param("inId") Long in_id);
    
    /* 문의 사항 목록 조회 */
    Page<SiteInquiry> findAllBySinDeleted(Pageable pageable, String deleted);

}
