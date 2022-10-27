package com.danakga.webservice.qna.site.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.qna.site.dto.request.ReqSiteQnADto;
import com.danakga.webservice.qna.site.dto.response.ResSiteQnADto;
import com.danakga.webservice.qna.site.model.SiteQnA;
import com.danakga.webservice.qna.site.repository.SiteQnARepository;
import com.danakga.webservice.qna.site.service.SiteQnAService;
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

        pageable = PageRequest.of(page, 10, Sort.by("siteQCreated").descending());
        Page<SiteQnA> siteQnA = siteQnARepository.findBySiteQDeleted(pageable, deleted);

        List<Map<String, Object>> siteQnAList = new ArrayList<>();

        siteQnA.forEach(entity -> {
            Map<String,Object> siteQnAMap = new LinkedHashMap<>();
            siteQnAMap.put("siteQ_id", entity.getSiteQId());
            siteQnAMap.put("siteQ_type", entity.getSiteQType());
            siteQnAMap.put("siteQ_userid", entity.getUserInfo().getUserid());
            siteQnAMap.put("siteQ_title", entity.getSiteQTitle());
            siteQnAMap.put("siteQ_created", entity.getSiteQCreated());
            siteQnAMap.put("siteQ_state", entity.getSiteQState());
            siteQnAMap.put("totalPage", siteQnA.getTotalPages());
            siteQnAMap.put("totalElement", siteQnA.getTotalPages());
            siteQnAList.add(siteQnAMap);
        });

        return new ResSiteQnADto(siteQnAList);
    }

    /* 문의사항 조회 */
    @Override
    public ResSiteQnADto siteQnAPost(Long siteQ_id) {

        SiteQnA checkSiteQnA = siteQnARepository.findById(siteQ_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>> siteQnAList = new ArrayList<>();

        Map<String, Object> siteQnAMap = new LinkedHashMap<>();

        siteQnAMap.put("q_id", checkSiteQnA.getSiteQId());
        siteQnAMap.put("q_type", checkSiteQnA.getSiteQType());
        siteQnAMap.put("q_userid", checkSiteQnA.getUserInfo().getUserid());
        siteQnAMap.put("q_title", checkSiteQnA.getSiteQTitle());
        siteQnAMap.put("q_content", checkSiteQnA.getSiteQContent());
        siteQnAMap.put("q_created", checkSiteQnA.getSiteQCreated());

        siteQnAList.add(siteQnAMap);

        return new ResSiteQnADto(siteQnAList);
    }

    /* 문의사항 작성 */
    @Transactional
    @Override
    public ResResultDto siteQnAWrite(UserInfo userInfo, ReqSiteQnADto reqSiteQnADto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA siteQnA = siteQnARepository.save(
                SiteQnA.builder()
                        .siteQType(reqSiteQnADto.getSiteQType())
                        .siteQTitle(reqSiteQnADto.getSiteQContent())
                        .siteQWriter(userInfo.getUserid())
                        .siteQContent(reqSiteQnADto.getSiteQContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(siteQnA.getSiteQId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto siteQnAEdit(UserInfo userInfo, ReqSiteQnADto reqQnADto, Long siteQ_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA checkSiteQnA = siteQnARepository.findById(siteQ_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkSiteQnA = siteQnARepository.save(
                SiteQnA.builder()
                        .siteQId(checkSiteQnA.getSiteQId())
                        .siteQType(reqQnADto.getSiteQType())
                        .siteQTitle(reqQnADto.getSiteQTitle())
                        .siteQContent(reqQnADto.getSiteQContent())
                        .siteQCreated(checkSiteQnA.getSiteQCreated())
                        .siteQDeleted(checkSiteQnA.getSiteQDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkSiteQnA.getSiteQId(), "문의 사항을 수정했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto siteQnADelete(UserInfo userInfo, Long siteQ_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        SiteQnA checkSiteQnA = siteQnARepository.findById(siteQ_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        /* 문의사항 삭제 상태 변경 (삭제) */
        siteQnARepository.updateSiteQnADeleted(checkSiteQnA.getSiteQId());

        return new ResResultDto(checkSiteQnA.getSiteQId(), "문의 사항을 삭제 했습니다.");
    }
}
