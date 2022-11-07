package com.danakga.webservice.admin.service.Impl;

import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminActivityServiceImpl implements AdminActivityService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /* 관리자 게시판 목록 */
    /* sort == deleted N, Y */
    @Override
    public ResBoardListDto adminBoardListDto(UserInfo userInfo, String sort, Pageable pageable, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), userInfo.getRole())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        pageable = PageRequest.of(page, 10, Sort.by("bdCreated").descending());
        Page<Board> checkBoard = boardRepository.findByBdDeleted(sort, pageable);

        List<Map<String,Object>> adminBoardList = new ArrayList<>();

        checkBoard.forEach(entity -> {

            Map<String,Object> adminBoardMap = new LinkedHashMap<>();

            adminBoardMap.put("bd_id", entity.getBdId());
            adminBoardMap.put("bd_type", entity.getBdType());
            adminBoardMap.put("bd_title", entity.getBdTitle());
            adminBoardMap.put("bd_writer", entity.getBdWriter());
            adminBoardMap.put("bd_created", entity.getBdCreated());
            adminBoardMap.put("bd_views", entity.getBdViews());
            adminBoardMap.put("bd_deleted", entity.getBdDeleted());
            adminBoardMap.put("totalElement", checkBoard.getTotalElements());
            adminBoardMap.put("totalPage", checkBoard.getTotalPages());

            adminBoardList.add(adminBoardMap);
        });

        return new ResBoardListDto(adminBoardList);
    }

    /* 게시글 삭제 */
    @Override
    public ResResultDto adminBoardDelete(UserInfo userInfo, Long bd_id) {

        return new ResResultDto(0L, "게시글을 삭제 했습니다.");
    }
}
