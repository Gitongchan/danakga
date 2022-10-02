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
    private final SiteInquiryRepository inquiryRepository;

    /* 문의사항 목록 */
    @Override
    public ResSiteInquiryDto siteInquiryList(Pageable pageable, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("inCreated").descending());
        Page<SiteInquiry> siteInquiry = inquiryRepository.findAllBySinDeleted(pageable, deleted);

        List<Map<String, Object>> inquiryList = new ArrayList<>();

        siteInquiry.forEach(entity -> {
            Map<String,Object> inquiryMap = new LinkedHashMap<>();
            inquiryMap.put("sin_id", entity.getSinId());
            inquiryMap.put("sin_userid", entity.getUserInfo().getUserid());
            inquiryMap.put("sin_title", entity.getSinTitle());
            inquiryMap.put("sin_created", entity.getSinCreated());
            inquiryMap.put("sin_state", entity.getSinState());
            inquiryMap.put("totalPage", siteInquiry.getTotalPages());
            inquiryMap.put("totalElement", siteInquiry.getTotalPages());
            inquiryList.add(inquiryMap);
        });

        return new ResSiteInquiryDto(inquiryList);
    }

    /* 문의사항 조회 */
    @Override
    public ResSiteInquiryDto siteInquiryPost(Long sin_id) {
        return null;
    }

    /* 문의사항 작성 */
    @Override
    public ResResultDto siteInquiryWrite(UserInfo userInfo, ReqSiteInquiryDto reqInquiryDto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteInquiry siteInquiry = inquiryRepository.save(
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

        SiteInquiry checkSiteInquiry = inquiryRepository.findById(sin_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkSiteInquiry = inquiryRepository.save(
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

        SiteInquiry checkSiteInquiry = inquiryRepository.findById(sin_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        inquiryRepository.updateInDeleted(checkSiteInquiry.getSinId());

        return new ResResultDto(checkSiteInquiry.getSinId(), "문의 사항을 삭제 했습니다.");
    }
}
