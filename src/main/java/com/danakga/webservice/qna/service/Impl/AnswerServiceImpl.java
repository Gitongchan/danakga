package com.danakga.webservice.qna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.dto.request.ReqAnswerDto;
import com.danakga.webservice.qna.model.Answer;
import com.danakga.webservice.qna.model.QnA;
import com.danakga.webservice.qna.repository.AnswerRepository;
import com.danakga.webservice.qna.repository.QnARepository;
import com.danakga.webservice.qna.service.AnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final QnARepository qnARepository;
    private final AnswerRepository answerRepository;

    @Override
    public ResResultDto shopAnswerWrite(UserInfo userInfo, ReqAnswerDto reqAnswerDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        QnA recentQnA = qnARepository.findById(reqAnswerDto.getAId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        if(recentUserInfo.getRole().equals(UserRole.ROLE_USER) && recentUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");
        }

        Answer answer = answerRepository.save(
                Answer.builder()
                        .aWriter(recentUserInfo.getUserid())
                        .aContent(reqAnswerDto.getAContent())
                        .qna(recentQnA)
                        .build()
        );

        return new ResResultDto(answer.getAId(),"답변을 작성 했습니다.");
    }
}
