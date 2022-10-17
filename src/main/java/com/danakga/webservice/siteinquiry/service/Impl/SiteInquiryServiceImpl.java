package com.danakga.webservice.siteinquiry.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.siteinquiry.dto.request.ReqSiteInquiryDto;
import com.danakga.webservice.siteinquiry.dto.response.ResSiteInquiryDto;
import com.danakga.webservice.siteinquiry.model.SiteInquiry;
import com.danakga.webservice.siteinquiry.repository.SiteInquiryRepository;
import com.danakga.webservice.siteinquiry.service.SiteInquiryService;
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
public class SiteInquiryServiceImpl implements SiteInquiryService {

    private final UserRepository userRepository;
    private final SiteInquiryRepository siteInquiryRepository;

    /* 문의사항 목록 */
    @Override
    public ResSiteInquiryDto siteInquiryList(Pageable pageable, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("sinCreated").descending());
        Page<SiteInquiry> siteInquiry = siteInquiryRepository.findAllBySinDeleted(pageable, deleted);

        List<Map<String, Object>> siteInquiryList = new ArrayList<>();

        siteInquiry.forEach(entity -> {
            Map<String,Object> siteInquiryMap = new LinkedHashMap<>();
            siteInquiryMap.put("sin_id", entity.getSinId());
            siteInquiryMap.put("sin_type", entity.getSinType());
            siteInquiryMap.put("sin_userid", entity.getUserInfo().getUserid());
            siteInquiryMap.put("sin_title", entity.getSinTitle());
            siteInquiryMap.put("sin_created", entity.getSinCreated());
            siteInquiryMap.put("sin_state", entity.getSinState());
            siteInquiryMap.put("totalPage", siteInquiry.getTotalPages());
            siteInquiryMap.put("totalElement", siteInquiry.getTotalPages());
            siteInquiryList.add(siteInquiryMap);
        });

        return new ResSiteInquiryDto(siteInquiryList);
    }

    /* 문의사항 조회 */
    @Override
    public ResSiteInquiryDto siteInquiryPost(Long sin_id) {

        SiteInquiry checkSiteInquiry = siteInquiryRepository.findById(sin_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>> postList = new ArrayList<>();

        Map<String, Object> postMap = new LinkedHashMap<>();

        postMap.put("sin_id", checkSiteInquiry.getSinId());
        postMap.put("sin_type", checkSiteInquiry.getSinType());
        postMap.put("sin_userid", checkSiteInquiry.getUserInfo().getUserid());
        postMap.put("sin_title", checkSiteInquiry.getSinTitle());
        postMap.put("sin_content", checkSiteInquiry.getSinContent());
        postMap.put("sin_created", checkSiteInquiry.getSinCreated());

        postList.add(postMap);

        return new ResSiteInquiryDto(postList);
    }

    /* 문의사항 작성 */
    @Override
    public ResResultDto siteInquiryWrite(UserInfo userInfo, ReqSiteInquiryDto reqInquiryDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteInquiry siteInquiry = siteInquiryRepository.save(
                SiteInquiry.builder()
                        .sinType(reqInquiryDto.getSinType())
                        .sinTitle(reqInquiryDto.getSinContent())
                        .sinContent(reqInquiryDto.getSinContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(siteInquiry.getSinId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto siteInquiryEdit(UserInfo userInfo, ReqSiteInquiryDto reqInquiryDto, Long sin_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteInquiry checkSiteInquiry = siteInquiryRepository.findById(sin_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkSiteInquiry = siteInquiryRepository.save(
                SiteInquiry.builder()
                        .sinId(checkSiteInquiry.getSinId())
                        .sinType(reqInquiryDto.getSinType())
                        .sinTitle(reqInquiryDto.getSinTitle())
                        .sinContent(reqInquiryDto.getSinContent())
                        .sinCreated(checkSiteInquiry.getSinCreated())
                        .sinDeleted(checkSiteInquiry.getSinDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkSiteInquiry.getSinId(), "문의 사항을 수정했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto siteInquiryDelete(UserInfo userInfo, Long sin_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteInquiry checkSiteInquiry = siteInquiryRepository.findById(sin_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        siteInquiryRepository.updateInDeleted(checkSiteInquiry.getSinId());

        return new ResResultDto(checkSiteInquiry.getSinId(), "문의 사항을 삭제 했습니다.");
    }
}
