package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.dto.request.ReqQnADto;
import com.danakga.webservice.qna.service.ShopAnswerService;
import com.danakga.webservice.qna.model.Answer;
import com.danakga.webservice.qna.model.QnA;
import com.danakga.webservice.qna.repository.AnswerRepository;
import com.danakga.webservice.qna.repository.QnARepository;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        QnA recentQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        if(checkUserInfo.getRole().equals(UserRole.ROLE_USER) && checkUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");
        }

        Answer answer = answerRepository.save(
                Answer.builder()
                        .aWriter(checkUserInfo.getUserid())
                        .aContent(reqAnswerDto.getAnswerContent())
                        .QnA(recentQnA)
                        .build()
        );

        /* 문의사항 답변 상태 변경 (작성 완료) */
        qnaRepository.updateQnAState(recentQnA.getQId());

        return new ResResultDto(answer.getAId(),"답변을 작성 했습니다.");
    }

    /* 가게 문의사항 답변 수정 */
    @Override
    public ResResultDto shopAnswerEdit(UserInfo userInfo, ReqQnADto reqQnADto, Long q_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("등록된 회원 정보를 찾을 수 없습니다."));

        QnA checkQnA = qnaRepository.findById(q_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        checkQnA = qnaRepository.save(
                QnA.builder()
                        .qId(checkQnA.getQId())
                        .qTitle(reqQnADto.getQnaTitle())
                        .qType(reqQnADto.getQnaType())
                        .qContent(reqQnADto.getQnaContent())
                        .qCreated(checkQnA.getQCreated())
                        .qDeleted(checkQnA.getQDeleted())
                        .userInfo(checkUserInfo)
                        .build()
        );

        return new ResResultDto(checkQnA.getQId(),"문의사항을 수정 했습니다.");
    }
}
