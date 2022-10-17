package com.danakga.webservice.siteqna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.siteqna.dto.request.ReqSiteQnADto;
import com.danakga.webservice.siteqna.dto.response.ResSiteQnADto;
import com.danakga.webservice.siteqna.model.SiteQnA;
import com.danakga.webservice.siteqna.repository.SiteQnARepository;
import com.danakga.webservice.siteqna.service.SiteQnAService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SiteQnAServiceImpl implements SiteQnAService {

    private final UserRepository userRepository;
    private final SiteQnARepository siteQnARepository;

    /* 문의사항 목록 */
    @Override
    public ResSiteQnADto siteQnAList(Pageable pageable, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("sqCreated").descending());
        Page<SiteQnA> siteQnA = siteQnARepository.findAllBySqDeleted(pageable, deleted);

        List<Map<String, Object>> siteQnAList = new ArrayList<>();

        siteQnA.forEach(entity -> {
            Map<String,Object> siteQnAMap = new LinkedHashMap<>();
            siteQnAMap.put("sq_id", entity.getSqId());
            siteQnAMap.put("sq_type", entity.getSqType());
            siteQnAMap.put("sq_userid", entity.getUserInfo().getUserid());
            siteQnAMap.put("sq_title", entity.getSqTitle());
            siteQnAMap.put("sq_created", entity.getSqCreated());
            siteQnAMap.put("sq_state", entity.getSqState());
            siteQnAMap.put("totalPage", siteQnA.getTotalPages());
            siteQnAMap.put("totalElement", siteQnA.getTotalPages());
            siteQnAList.add(siteQnAMap);
        });

        return new ResSiteQnADto(siteQnAList);
    }

    /* 문의사항 조회 */
    @Override
    public ResSiteQnADto siteQnAPost(Long sq_id) {

        SiteQnA checkSiteQnA = siteQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>> siteQnAList = new ArrayList<>();

        Map<String, Object> siteQnAMap = new LinkedHashMap<>();

        siteQnAMap.put("sin_id", checkSiteQnA.getSqId());
        siteQnAMap.put("sin_type", checkSiteQnA.getSqType());
        siteQnAMap.put("sin_userid", checkSiteQnA.getUserInfo().getUserid());
        siteQnAMap.put("sin_title", checkSiteQnA.getSqTitle());
        siteQnAMap.put("sin_content", checkSiteQnA.getSqContent());
        siteQnAMap.put("sin_created", checkSiteQnA.getSqCreated());

        siteQnAList.add(siteQnAMap);

        return new ResSiteQnADto(siteQnAList);
    }

    /* 문의사항 작성 */
    @Override
    public ResResultDto siteQnAWrite(UserInfo userInfo, ReqSiteQnADto reqInquiryDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA siteInquiry = siteQnARepository.save(
                SiteQnA.builder()
                        .sqType(reqInquiryDto.getSqType())
                        .sqTitle(reqInquiryDto.getSqContent())
                        .sqContent(reqInquiryDto.getSqContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(siteInquiry.getSqId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto siteQnAEdit(UserInfo userInfo, ReqSiteQnADto reqInquiryDto, Long sq_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA checkSiteInquiry = siteQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkSiteInquiry = siteQnARepository.save(
                SiteQnA.builder()
                        .sqId(checkSiteInquiry.getSqId())
                        .sqType(reqInquiryDto.getSqType())
                        .sqTitle(reqInquiryDto.getSqTitle())
                        .sqContent(reqInquiryDto.getSqContent())
                        .sqCreated(checkSiteInquiry.getSqCreated())
                        .sqDeleted(checkSiteInquiry.getSqDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkSiteInquiry.getSqId(), "문의 사항을 수정했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto siteQnADelete(UserInfo userInfo, Long sq_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA checkSiteQnA = siteQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        siteQnARepository.updateInDeleted(checkSiteQnA.getSqId());

        return new ResResultDto(checkSiteQnA.getSqId(), "문의 사항을 삭제 했습니다.");
    }
}
