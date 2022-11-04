package com.danakga.webservice.qna.repository;

import com.danakga.webservice.qna.model.Answer;
import com.danakga.webservice.qna.model.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /* 문의사항 답변 조회 */
    Optional<Answer> findByAnIdAndQna(Long an_id, Qna qna);
    
    /* 문의사항 답변 조회 (삭제 상태로 변경 후 사용) */
    List<Answer> findByAnDeletedAndQna(String deleted, Qna qna);

    /* 가게 문의사항 답변 삭제 상태 변경 */
    @Transactional
    @Modifying
    @Query("update Answer a set a.anDeleted = 'Y' where a.anId = :anId")
    void updateAnswerDeleted(@Param("anId") Long an_id);

}
