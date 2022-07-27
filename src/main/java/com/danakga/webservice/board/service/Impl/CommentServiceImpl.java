package com.danakga.webservice.board.service.Impl;

import com.danakga.webservice.board.dto.request.ReqCommentDto;
import com.danakga.webservice.board.dto.response.ResCommentListDto;
import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import com.danakga.webservice.board.repository.BoardRepository;
import com.danakga.webservice.board.repository.CommentRepository;
import com.danakga.webservice.board.service.CommentService;
import com.danakga.webservice.exception.CustomException;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
    @Override
    public ResCommentListDto commentsList(Long id, Pageable pageable, int page) {

        //삭제여부가 "N"의 값만 가져오기 위한 변수
        final String deleted = "N";

        //해당 bdid값이 없다면 exception 값 전달
        Board board = boardRepository.findByBdId(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글 찾을 수 없습니다."));

        //bd_id, deleted = N 값인 댓글만 페이징해서 조회
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> board_comments = commentRepository.findAllByBoardAndCmDeleted(board, deleted, pageable);

        //paging한 baord_comments를 .getContent()하여 리스트로 변환하여 담아줌
        List<Board_Comment> commentList = board_comments.getContent();

        //Dto에 값을 담아주기 위한 List<Map>
        List<Map<String, Object>> mapComments = new ArrayList<>();

        //return해줄 Dto 객체 생성
        ResCommentListDto resCommentListDto = new ResCommentListDto();

        //List 값을 반복문으로 Map에 담아줌
        commentList.forEach(entity -> {
            Map<String, Object> commentsMap = new HashMap<>();
            commentsMap.put("cm_id", entity.getCmId());
            commentsMap.put("cm_content", entity.getCmContent());
            commentsMap.put("cm_writer", entity.getCmWriter());
            commentsMap.put("cm_created", entity.getCmCreated());
            commentsMap.put("cm_modify", entity.getCmModified());
            commentsMap.put("totalElement", board_comments.getTotalElements());
            commentsMap.put("totalPage", board_comments.getTotalPages());
            mapComments.add(commentsMap);
        });

        //Dto 값 set
        resCommentListDto.setComments(mapComments);

        return resCommentListDto;
    }

    //댓글 작성
    //cmGroup을 가장 큰 group값 +1 해줘야 함
    //cmStep은 댓글 0, 대댓글 1
    @Override
    public Long commentsWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id) {

        //게시글 먼저 있는지 확인 후 회원 정보와 게시글 db 가져옴
        if (boardRepository.findById(bd_id).isPresent()) {

            if (userRepository.findById(userInfo.getId()).isPresent()) {

                Board recentBoard = boardRepository.findById(bd_id).get();

                UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                Board_Comment board_Comment;

                Integer groupCount = commentRepository.maxGroupValue();

                if(groupCount == null) {
                    groupCount = 0;
                }

                commentRepository.save(
                        board_Comment = Board_Comment.builder()
                                .cmContent(reqCommentDto.getCmContent())
                                .cmWriter(recentUserInfo.getUserid())
                                .cmGroup(groupCount + 1)
                                .cmStep(0)
                                .cmDepth(0)
                                .cmAnswerNum(0)
                                .cmParentNum(0)
                                .board(recentBoard)
                                .userInfo(recentUserInfo)
                                .build()
                );
                return board_Comment.getCmId();
            }
        }
        return -1L;
    }

    //댓글 수정
    @Override
    public Long commentsEdit(Long bd_id, Long cm_id, UserInfo userInfo, ReqCommentDto reqCommentDto) {

        //회원 정보 확인 후 게시글 가져오기
        if (userRepository.findById(userInfo.getId()).isPresent()) {

            if (boardRepository.findByBdId(bd_id).isPresent()) {

                Board recentBoard = boardRepository.findByBdId(bd_id).get();

                UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                Board_Comment board_Comment = commentRepository.findByBoardAndCmId(recentBoard, cm_id);

                commentRepository.save(
                        board_Comment = Board_Comment.builder()
                                .cmContent(reqCommentDto.getCmContent())
                                .cmWriter(recentUserInfo.getUserid())
                                .cmDeleted(board_Comment.getCmDeleted())
                                .cmCreated(board_Comment.getCmCreated())
                                .cmModified(board_Comment.getCmModified())
                                .userInfo(recentUserInfo)
                                .board(recentBoard)
                                .build()
                );
                return board_Comment.getCmId();
            }
        }
        return -1L;
    }

    //댓글 삭제 여부 변경
    @Override
    public Long commentsDelete(Long bd_id, Long cm_id, UserInfo userInfo) {

        //해당 id의 null값 체크 후 값이 있으면  deleted 값 변경
        if (boardRepository.findByBdId(bd_id).isPresent()) {

            if (userRepository.findById(userInfo.getId()).isPresent()) {

                Board board = boardRepository.findByBdId(bd_id).get();

                Board_Comment board_Comment = commentRepository.findByBoardAndCmId(board, cm_id);

                //deleted 값 변경
                commentRepository.updateDeleted(cm_id);

                return board_Comment.getCmId();
            }
        }
        return -1L;
    }

    //개별 댓글 조회
    @Override
    public ResCommentListDto writeComments(Long cm_id) {

        Board_Comment comments = commentRepository.findByCmId(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 댓글을 찾을 수 없습니다."));

        //Dto에 값을 담아주기 위한 List<Map>
        List<Map<String, Object>> mapComments = new ArrayList<>();

        //return해줄 Dto 객체 생성
        ResCommentListDto resCommentListDto = new ResCommentListDto();

        //map에 댓글 정보 put
        Map<String, Object> writeComments = new HashMap<>();
        writeComments.put("cm_id", comments.getCmId());
        writeComments.put("cm_comment", comments.getCmContent());
        writeComments.put("cm_created", comments.getCmCreated());
        writeComments.put("cm_deleted", comments.getCmDeleted());
        writeComments.put("cm_modified", comments.getCmModified());
        writeComments.put("cm_writer", comments.getCmWriter());
        mapComments.add(writeComments);

        //Dto 값 set
        resCommentListDto.setComments(mapComments);

        return resCommentListDto;
    }

    //대댓글 작성
    //대댓글 작성 시 cmAnswerNum(대댓글 갯수)도 update 시켜야함
    //cmStep(댓글의 계층)은 탬플릿이 대댓글까지 밖에 안돼서 댓글 0, 대댓글 1로 고정
    //cmGroup(댓글의 그룹)은 가장 큰 그룹 값 찾아서 +1 해야함
    @Override
    public Long answerWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id) {

        if(userRepository.findById(userInfo.getId()).isPresent()) {

            if(boardRepository.findById(bd_id).isPresent()) {

                if(commentRepository.findById(cm_id).isPresent()) {

                    //Long값의 댓글 번호를 int로 형변환
                    final int parentNum = cm_id.intValue();
                    
                    UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                    Board recentBoard = boardRepository.findById(bd_id).get();

                    Board_Comment board_Comment = commentRepository.findById(cm_id).get();

                    Integer depthCount = commentRepository.maxDepthValue(board_Comment.getCmGroup());

                    commentRepository.save(
                            board_Comment = Board_Comment.builder()
                                    .cmContent(reqCommentDto.getCmContent())
                                    .cmWriter(recentUserInfo.getUserid())
                                    .cmGroup(board_Comment.getCmGroup())
                                    .cmStep(1)
                                    .cmDepth(depthCount + 1)
                                    .cmParentNum(parentNum)
                                    .board(recentBoard)
                                    .userInfo(recentUserInfo)
                                    .build()
                    );

                    //댓글의 대댓글갯수 증가
                    commentRepository.updateAnswerNum(cm_id);

                    return board_Comment.getCmId();
                }
            }
        }
        return -1L;
    }
}
