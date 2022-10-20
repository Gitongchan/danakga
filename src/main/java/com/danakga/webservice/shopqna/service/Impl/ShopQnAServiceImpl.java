package com.danakga.webservice.shopqna.service.Impl;

import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.shopqna.dto.request.ReqShopQnADto;
import com.danakga.webservice.shopqna.dto.response.ResShopQnADto;
import com.danakga.webservice.shopqna.model.ShopQnA;
import com.danakga.webservice.shopqna.repository.ShopQnARepository;
import com.danakga.webservice.shopqna.service.ShopQnAService;
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
public class ShopQnAServiceImpl implements ShopQnAService {

    private final UserRepository userRepository;
    private final ShopQnARepository shopQnARepository;

    /* 문의사항 목록 */
    @Override
    public ResShopQnADto shopQnAList(Pageable pageable, int page) {

        final String deleted = "N";

        pageable = PageRequest.of(page, 10, Sort.by("sqCreated").descending());
        Page<ShopQnA> shopQnA = shopQnARepository.findAllBySqDeleted(pageable, deleted);

        List<Map<String, Object>> shopQnAList = new ArrayList<>();

        shopQnA.forEach(entity -> {
            Map<String,Object> shopQnAMap = new LinkedHashMap<>();
            shopQnAMap.put("sq_id", entity.getSqId());
            shopQnAMap.put("sq_type", entity.getSqType());
            shopQnAMap.put("sq_userid", entity.getUserInfo().getUserid());
            shopQnAMap.put("sq_title", entity.getSqTitle());
            shopQnAMap.put("sq_created", entity.getSqCreated());
            shopQnAMap.put("sq_state", entity.getSqState());
            shopQnAMap.put("totalPage", shopQnA.getTotalPages());
            shopQnAMap.put("totalElement", shopQnA.getTotalPages());
            shopQnAList.add(shopQnAMap);
        });

        return new ResShopQnADto(shopQnAList);
    }

    /* 문의사항 조회 */
    @Override
    public ResShopQnADto shopQnAPost(Long sq_id) {

        ShopQnA checkShopQnA = shopQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의사항을 찾을 수 없습니다."));

        List<Map<String,Object>>shopQnAList = new ArrayList<>();

        Map<String, Object> shopQnAMap = new LinkedHashMap<>();

        shopQnAMap.put("sin_id", checkShopQnA.getSqId());
        shopQnAMap.put("sin_type", checkShopQnA.getSqType());
        shopQnAMap.put("sin_userid", checkShopQnA.getUserInfo().getUserid());
        shopQnAMap.put("sin_title", checkShopQnA.getSqTitle());
        shopQnAMap.put("sin_content", checkShopQnA.getSqContent());
        shopQnAMap.put("sin_created", checkShopQnA.getSqCreated());

        shopQnAList.add(shopQnAMap);

        return new ResShopQnADto(shopQnAList);
    }

    /* 문의사항 작성 */
    @Override
    public ResResultDto shopQnAWrite(UserInfo userInfo, ReqShopQnADto reqShopQnADto) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        ShopQnA shopQnA = shopQnARepository.save(
                ShopQnA.builder()
                        .sqType(reqShopQnADto.getSqType())
                        .sqTitle(reqShopQnADto.getSqContent())
                        .sqContent(reqShopQnADto.getSqContent())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(shopQnA.getSqId(), "문의 사항을 작성 했습니다.");
    }
    
    /* 문의사항 수정 */
    @Transactional
    @Override
    public ResResultDto shopQnAEdit(UserInfo userInfo, ReqShopQnADto reqShopQnADto, Long sq_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        ShopQnA checkShopQnA = shopQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        checkShopQnA = shopQnARepository.save(
                ShopQnA.builder()
                        .sqId(checkShopQnA.getSqId())
                        .sqType(reqShopQnADto.getSqType())
                        .sqTitle(reqShopQnADto.getSqTitle())
                        .sqContent(reqShopQnADto.getSqContent())
                        .sqCreated(checkShopQnA.getSqCreated())
                        .sqDeleted(checkShopQnA.getSqDeleted())
                        .userInfo(recentUserInfo)
                        .build()
        );

        return new ResResultDto(checkShopQnA.getSqId(), "문의 사항을 수정했습니다.");
    }

    /* 문의사항 삭제 상태 변경 */
    @Transactional
    @Override
    public ResResultDto shopQnADelete(UserInfo userInfo, Long sq_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        ShopQnA checkShopQnA = shopQnARepository.findById(sq_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("문의 사항을 찾을 수 없습니다."));

        shopQnARepository.updateInDeleted(checkShopQnA.getSqId());

        return new ResResultDto(checkShopQnA.getSqId(), "문의 사항을 삭제 했습니다.");
    }
}
