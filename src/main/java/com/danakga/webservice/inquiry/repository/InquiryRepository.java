package com.danakga.webservice.inquiry.repository;

import com.danakga.webservice.inquiry.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    /* 문의 사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update Inquiry i set i.inDeleted = 'Y' where i.inId = :inId")
    void updateInDeleted(@Param("inId") Long in_id);

}
