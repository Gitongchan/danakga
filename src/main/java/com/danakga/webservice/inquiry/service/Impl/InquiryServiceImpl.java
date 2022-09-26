package com.danakga.webservice.inquiry.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.inquiry.dto.request.ReqInquiryDto;
import com.danakga.webservice.inquiry.model.Inquiry;
import com.danakga.webservice.inquiry.repository.InquiryRepository;
import com.danakga.webservice.inquiry.service.InquiryService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public ResResultDto inquiryWrite(UserInfo userInfo, ReqInquiryDto reqInquiryDto) {

        UserInfo recentUser = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Inquiry inquiry = inquiryRepository.save(
                Inquiry.builder()
                        .inType(reqInquiryDto.getInType())
                        .inTitle(reqInquiryDto.getInContent())
                        .inContent(reqInquiryDto.getInContent())
                        .userInfo(recentUser)
                        .build()
        );

        return new ResResultDto(inquiry.getInId(), "문의 사항을 작성 했습니다.");
    }
}
