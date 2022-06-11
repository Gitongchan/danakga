package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.exception.CustomException;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.board.service.CommentService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private Board_Comment board_Comment;

    //댓글 조회
    @Override
    public ResCommentListDto commentList(Long id, Pageable pageable, int page) {

        final String deleted = "N";

        Board board = boardRepository.findByBdId(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글을 찾을 수 없습니다."));

        //cm_id desc 정렬
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> board_comments = commentRepository.findAllByBoardAndCmDeleted(board, deleted, pageable);

        List<Board_Comment> commentList = board_comments.getContent();

        List<Map<String, Object>> mapComments = new ArrayList<>();

        ResCommentListDto resCommentListDto = new ResCommentListDto();

        commentList.forEach(entity -> {
            Map<String, Object> commentsMap = new HashMap<>();
            commentsMap.put("cm_id", entity.getCmId());
            commentsMap.put("cm_content", entity.getCmComment());
            commentsMap.put("cm_writer", entity.getCmWriter());
            commentsMap.put("cm_created", entity.getCmCreated());
            commentsMap.put("cm_modify", entity.getCmModified());
            mapComments.add(commentsMap);
        });
        resCommentListDto.setComments(mapComments);

        return resCommentListDto;
    }

    //댓글 작성
    @Override
    public Long commentWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long id) {

        //게시글 먼저 있는지 확인 후 회원 정보와 게시글 db 가져옴
        if (boardRepository.findById(id).isPresent()) {
            if (userRepository.findById(userInfo.getId()).isPresent()) {

                Board recentBoard = boardRepository.findById(id).get();

                UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                commentRepository.save(
                        board_Comment = Board_Comment.builder()
                                .cmComment(reqCommentDto.getCmContent())
                                .cmWriter(recentUserInfo.getUserid())
                                .board(recentBoard)
                                .userInfo(recentUserInfo)
                                .build()
                );
                return board_Comment.getCmId();
            }
        }
        return -1L;
    }


}

