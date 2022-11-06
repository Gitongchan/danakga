package com.danakga.webservice.qna.repository;

import com.danakga.webservice.company.model.CompanyInfo;
import com.danakga.webservice.qna.model.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface QnaRepository extends JpaRepository<Qna, Long> {

    /* 문의사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qnDeleted = 'Y' where q.qnId = :qnId")
    void updateQnADeleted(@Param("qnId") Long qn_id);

    /* 문의사항 답변 상태 변경 (작성 완료) */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qnState = 1 where q.qnId =:qnId")
    void updateQnaCompleteState(@Param("qnId") Long qn_id);

    /* 문의사항 답변 상태 변경 (작성 대기) */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qnState = 0 where q.qnId =:qnId")
    void updateQnaStandByState(@Param("qnId") Long qn_id);
    
    /* 사이트 문의사항 목록 조회 */
    Page<Qna> findByQnDeletedAndQnSort(Pageable pageable, String deleted, int q_sort);

    /* 가게 문의사항 목록 조회 */
    Page<Qna> findByQnDeletedAndCompanyInfo(String deleted, CompanyInfo companyInfo, Pageable pageable);

}
