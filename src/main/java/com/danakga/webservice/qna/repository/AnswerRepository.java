package com.danakga.webservice.qna.repository;

import com.danakga.webservice.qna.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
