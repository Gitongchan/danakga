package com.danakga.webservice.qna.site.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.site.dto.request.ReqSiteAnswerDto;
import com.danakga.webservice.qna.site.model.SiteAnswer;
import com.danakga.webservice.qna.site.model.SiteQnA;
import com.danakga.webservice.qna.site.repository.SiteAnswerRepository;
import com.danakga.webservice.qna.site.repository.SiteQnARepository;
import com.danakga.webservice.qna.site.service.SiteAnswerService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteAnswerServiceImpl implements SiteAnswerService {

    private final UserRepository userRepository;
    private final SiteQnARepository siteQnARepository;
    private final SiteAnswerRepository answerRepository;

    /* 가게 문의사항 답변 작성 */
    @Override
    public ResResultDto siteAnswerWrite(UserInfo userInfo, ReqSiteAnswerDto reqAnswerDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA recentQnA = siteQnARepository.findById(reqAnswerDto.getSiteAId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        if(recentUserInfo.getRole().equals(UserRole.ROLE_USER) && recentUserInfo.getRole().equals(UserRole.ROLE_ADMIN)) {
            return new ResResultDto(-1L, "가게 매니저만 작성할 수 있습니다.");
        }

        SiteAnswer answer = answerRepository.save(
                SiteAnswer.builder()
                        .siteAWriter(recentUserInfo.getUserid())
                        .siteAContent(reqAnswerDto.getSiteAContent())
                        .siteQnA(recentQnA)
                        .build()
        );

        /* 문의사항 답변 상태 변경 (작성 완료) */
        siteQnARepository.updateSiteQnAState(recentQnA.getSiteQId());

        return new ResResultDto(answer.getSiteAId(),"답변을 작성 했습니다.");
    }
}
