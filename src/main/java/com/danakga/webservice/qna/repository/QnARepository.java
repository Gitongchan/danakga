package com.danakga.webservice.qna.repository;

import com.danakga.webservice.qna.model.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface QnARepository extends JpaRepository<Qna, Long> {

    /* 문의사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qDeleted = 'Y' where q.qId = :qId")
    void updateQnADeleted(@Param("qId") Long q_id);

    /* 문의사항 답변 상태 변경 (작성 완료) */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qState = 1 where q.qId =:qId")
    void updateQnaCompleteState(@Param("qId") Long q_id);

    /* 문의사항 답변 상태 변경 (작성 대기) */
    @Transactional
    @Modifying
    @Query("update Qna q set q.qState = 0 where q.qId =:qId")
    void updateQnaStandByState(@Param("qId") Long q_id);
    
    
    /* 문의 사항 목록 조회 */
    Page<Qna> findByQDeletedAndQSort(Pageable pageable, String deleted, int q_sort);

}
