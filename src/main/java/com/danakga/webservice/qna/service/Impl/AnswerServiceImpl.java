package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.service.ShopAnswerService;
import com.danakga.webservice.qna.model.Answer;
import com.danakga.webservice.qna.model.Qna;
import com.danakga.webservice.qna.repository.AnswerRepository;
import com.danakga.webservice.qna.repository.QnARepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements ShopAnswerService {

    private final UserRepository userRepository;
    private final QnARepository qnaRepository;
    private final AnswerRepository answerRepository;

    /* 가게 문의사항 답변 작성 */
    @Override
    public ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long q_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        if(checkUserInfo.getRole().equals(UserRole.ROLE_USER) && checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");
        }

        Answer answer = answerRepository.save(
                Answer.builder()
                        .anWriter(checkUserInfo.getUserid())
                        .anContent(reqAnswerDto.getAnswerContent())
                        .qna(checkQnA)
                        .build()
        );

        /* 문의사항 답변 상태 변경 (작성 완료) */
        qnaRepository.updateQnaCompleteState(checkQnA.getQId());

        return new ResResultDto(answer.getAnId(),"답변을 작성 했습니다.");
    }

    /* 가게 문의사항 답변 수정 */
    @Override
    public ResResultDto shopAnswerEdit(UserInfo userInfo, ReqAnswerDto reqAnswerDto, Long q_id, Long a_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(a_id, checkQnA)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        if(checkUserInfo.getRole().equals(UserRole.ROLE_USER) && checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 수정할 수 있습니다.");
        }

        Answer answer = answerRepository.save(
                Answer.builder()
                        .anId(checkAnswer.getAnId())
                        .anWriter(checkUserInfo.getUserid())
                        .anContent(reqAnswerDto.getAnswerContent())
                        .anCreated(checkAnswer.getAnCreated())
                        .anDeleted(checkAnswer.getAnDeleted())
                        .qna(checkQnA)
                        .build()
        );

        return new ResResultDto(answer.getAnId(),"답변을 수정 했습니다.");
    }

    /* 가게 문의사항 답변 삭제 */
    @Override
    public ResResultDto shopAnswerDelete(UserInfo userInfo, Long q_id, Long a_id) {

        final String deleted = "N";

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Qna checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        Answer checkAnswer = answerRepository.findByAnIdAndQna(a_id, checkQnA)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("답변을 찾을 수 없습니다."));

        if(checkUserInfo.getRole().equals(UserRole.ROLE_USER) && checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 삭제할 수 있습니다.");
        }

        answerRepository.updateAnswerDeleted(checkAnswer.getAnId());
        
        /* 삭제 상태로 변경 후 답변이 아예 없는 경우 문의사항을 다시 답변 대기 상태로 변경 */
        List<Answer> stateAnswer = answerRepository.findByAnDeleted(deleted);

        if(stateAnswer.isEmpty()) {
            qnaRepository.updateQnaStandByState(checkQnA.getQId());
        }

        return new ResResultDto(checkAnswer.getAnId(), "답변을 삭제 했습니다.");
    }
}
