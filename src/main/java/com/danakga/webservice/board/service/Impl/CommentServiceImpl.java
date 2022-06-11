package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.board.service.CommentService;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    @Override
    public Long commentWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long id) {

        //회원, 게시글 정보를 가장 최신의 db 값으로 받아옴
        if(userRepository.findById(userInfo.getId()).isPresent()) {

            UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

            if (boardRepository.findById(id).isEmpty()) {
                return -1L;
            }
            Board recentBoard = boardRepository.findById(id).get();

            Board_Comment board_Comment;

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
        return -1L;
    }
}
