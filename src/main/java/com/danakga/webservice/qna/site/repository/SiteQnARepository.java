package com.danakga.webservice.qna.site.repository;

import com.danakga.webservice.qna.site.model.SiteQnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SiteQnARepository extends JpaRepository<SiteQnA, Long> {

    /* 문의사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update SiteQnA site set site.siteQDeleted = 'Y' where site.siteQId = :siteQId")
    void updateSiteQnADeleted(@Param("siteQId") Long siteQ_id);

    /* 문의사항 답변 상태 변경 (작성 완료) */
    @Query("update SiteQnA site set site.siteQState = 1 where site.siteQId =:siteQId")
    void updateSiteQnAState(@Param("siteQId") Long siteQ_id);
    
    
    /* 문의 사항 목록 조회 */
    Page<SiteQnA> findBySiteQDeleted(Pageable pageable, String deleted);

}
