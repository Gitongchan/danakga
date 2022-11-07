package com.danakga.webservice.admin.service;

import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

public interface AdminActivityService {

    /* 관리자 게시판 목록 */
    ResBoardListDto adminBoardListDto(UserInfo userInfo, String sort, Pageable pageable, int page);

    /* 게시글 삭제 */
    ResResultDto adminBoardDelete(UserInfo userInfo, Long bd_id);
}
