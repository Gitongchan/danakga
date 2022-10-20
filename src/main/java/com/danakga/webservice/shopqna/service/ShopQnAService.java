package com.danakga.webservice.shopqna.service;

import com.danakga.webservice.shopqna.dto.request.ReqShopQnADto;
import com.danakga.webservice.shopqna.dto.response.ResShopQnADto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface ShopQnAService {
    
    /* 문의사항 목록 */
    ResShopQnADto shopQnAList(Pageable pageable, int page);

    /* 문의사항 조회 */
    ResShopQnADto shopQnAPost(Long sq_id);

    /* 문의사항 작성 */
    ResResultDto shopQnAWrite(UserInfo userInfo, ReqShopQnADto reqShopQnADto);

    /* 문의사항 수정 */
    ResResultDto shopQnAEdit(UserInfo userInfo, ReqShopQnADto reqShopQnADto, Long sq_id);

    /* 문의사항 삭제 상태 변경 */
    ResResultDto shopQnADelete(UserInfo userInfo, Long sq_id);

}
