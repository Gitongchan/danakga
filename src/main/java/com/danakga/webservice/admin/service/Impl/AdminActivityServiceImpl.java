package com.danakga.webservice.admin.service.Impl;

import com.danakga.webservice.admin.service.AdminActivityService;
import com.danakga.webservice.board.dto.response.ResBoardListDto;
import com.danakga.webservice.board.dto.response.ResBoardPostDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.board.repository.BoardFileRepository;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminActivityServiceImpl implements AdminActivityService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    /* 관리자 게시판 목록 */
    /* sort == deleted N, Y */
    @Override
    public ResBoardListDto adminBoardListDto(UserInfo userInfo, String sort, Pageable pageable, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
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

    /* 관리자 게시글 조회 */
    @Override
    public ResBoardPostDto adminBoardPost(UserInfo userInfo, Long bd_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        List<Board_Files> checkFiles = boardFileRepository.findByBoard(checkBoard);

        List<Map<String, Object>> postList = new ArrayList<>();

        List<Map<String, Object>> fileList = new ArrayList<>();

        checkFiles.forEach(entity -> {
            Map<String, Object> filesMap = new HashMap<>();
            filesMap.put("file_name", entity.getFileSaveName());
            filesMap.put("file_path", entity.getFilePath());
            fileList.add(filesMap);
        });

        //게시글 정보 담을 Map
        Map<String, Object> postMap = new LinkedHashMap<>();

        postMap.put("bd_id", checkBoard.getBdId());
        postMap.put("bd_type", checkBoard.getBdType());
        postMap.put("bd_writer", checkBoard.getBdWriter());
        postMap.put("bd_title", checkBoard.getBdTitle());
        postMap.put("bd_content", checkBoard.getBdContent());
        postMap.put("bd_created", checkBoard.getBdCreated());
        postMap.put("bd_modified", checkBoard.getBdModified());
        postMap.put("bd_views", checkBoard.getBdViews());
        postMap.put("bd_deleted", checkBoard.getBdDeleted());
        postMap.put("files", fileList);

        postList.add(postMap);

        return new ResBoardPostDto(postList);
    }
    
    /* 관리자 게시판 검색 */

    @Override
    public ResBoardListDto adminBoardSearch(UserInfo userInfo, Pageable pageable, int page, String category, String sort, String content) {

        return new ResBoardListDto();
    }

    /* 게시글 삭제 */
    @Override
    public ResResultDto adminPostDelete(UserInfo userInfo, Long bd_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("어드민 사용자가 아닙니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        /* 게시글 삭제 */
        boardRepository.deleteById(checkBoard.getBdId());

        return new ResResultDto(checkBoard.getBdId(), "게시글을 삭제 했습니다.");
    }
}
