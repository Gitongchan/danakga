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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;


    /* 문의 사항 작성 */
    @Override
    public ResResultDto inquiryWrite(UserInfo userInfo, ReqInquiryDto reqInquiryDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Inquiry inquiry = inquiryRepository.save(
                Inquiry.builder()
                        .inType(reqInquiryDto.getInType())
                        .inTitle(reqInquiryDto.getInContent())
                        .inContent(reqInquiryDto.getInContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(inquiry.getInId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의 사항 수정 */
    @Transactional
    @Override
    public ResResultDto inquiryEdit(UserInfo userInfo, ReqInquiryDto reqInquiryDto, Long in_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Inquiry checkInquiry = inquiryRepository.findById(in_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkInquiry = inquiryRepository.save(
                Inquiry.builder()
                        .inId(checkInquiry.getInId())
                        .inType(reqInquiryDto.getInType())
                        .inTitle(reqInquiryDto.getInTitle())
                        .inContent(reqInquiryDto.getInContent())
                        .inCreated(checkInquiry.getInCreated())
                        .inDeleted(checkInquiry.getInDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkInquiry.getInId(), "문의 사항을 수정했습니다.");
    }

    @Transactional
    @Override
    public ResResultDto inquiryDelete(UserInfo userInfo, Long in_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Inquiry checkInquiry = inquiryRepository.findById(in_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        inquiryRepository.updateInDeleted(checkInquiry.getInId());

        return new ResResultDto(checkInquiry.getInId(), "문의 사항을 삭제 했습니다.");
    }
}
