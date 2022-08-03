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
    public ResCommentListDto commentsList(Long bd_id, Pageable pageable, int page) {

        //삭제여부가 "N"의 값만 가져오기 위한 변수
        final String deleted = "N";
        final int commentStep = 0;
        final int answerStep = 1;

        //해당 bd_id값이 없다면 exception 값 전달
        Board board = boardRepository.findByBdId(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("해당 게시글 찾을 수 없습니다."));

        //step이 0값 == 댓글 조회
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> comments = commentRepository.findAllByBoardAndCmDeletedAndCmStep(board, deleted, commentStep, pageable);

        //Dto에 값을 담아주기 위한 List<Map>
        List<Map<String, Object>> mapComments = new ArrayList<>();

        //return해줄 Dto 객체 생성
        ResCommentListDto resCommentListDto = new ResCommentListDto();


        //List 값을 반복문으로 Map에 담아줌
        //이 부분이 너무 어려웠다.
        comments.forEach(comment -> {

            //댓글 담을 map (LinkedHashMap은 키값의 순서를 보장하기 위한 HashMap)
            Map<String, Object> commentsMap = new LinkedHashMap<>();

            //대댓글 담을 List<map>
            List<Map<String, Object>> answersList = new ArrayList<>();

            //DB를 조회할때 forEach로 반복되는 댓글의 id값을 받아서 각 댓글의 대댓글을 조회
            Pageable answerPage = PageRequest.of(page, 10);
            Page<Board_Comment> answers = commentRepository.answerList(comment.getCmId(), answerStep, deleted, answerPage);

            answers.forEach(answer -> {

                Map<String, Object> answersMap = new HashMap<>();

                answersMap.put("cm_id", answer.getCmId());
                answersMap.put("cm_content", answer.getCmContent());
                answersMap.put("cm_writer", answer.getCmWriter());
                answersMap.put("cm_step", answer.getCmStep());
                answersMap.put("cm_parentNum", answer.getCmParentNum());
                answersMap.put("cm_created", answer.getCmCreated());
                answersMap.put("cm_modify", answer.getCmModified());
                answersList.add(answersMap);
            });

            commentsMap.put("cm_id", comment.getCmId());
            commentsMap.put("cm_content", comment.getCmContent());
            commentsMap.put("cm_writer", comment.getCmWriter());
            commentsMap.put("cm_step", comment.getCmStep());
            commentsMap.put("cm_created", comment.getCmCreated());
            commentsMap.put("cm_modify", comment.getCmModified());
            commentsMap.put("cm_answerNum", comment.getCmAnswerNum());
            commentsMap.put("totalElement", comments.getTotalElements());
            commentsMap.put("totalPage", comments.getTotalPages());
            commentsMap.put("answer", answersList);
            mapComments.add(commentsMap);
        });
        //Dto 값 set
        resCommentListDto.setComments(mapComments);

        return resCommentListDto;
    }

    //댓글 작성
    @Override
    public Long commentsWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id) {

        //게시글 먼저 있는지 확인 후 회원 정보와 게시글 db 가져옴
        if (userRepository.findById(userInfo.getId()).isPresent()) {

            if (boardRepository.findById(bd_id).isPresent()) {

                Board recentBoard = boardRepository.findById(bd_id).get();

                UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                Board_Comment board_Comment;

                // null값 비교를 위해 Integer 사용
                Integer groupCount = commentRepository.maxGroupValue();

                //댓글이 처음 작성 됐을 때를 위한 초기 값 설정
                if (groupCount == null) {
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
                                .cmId(board_Comment.getCmId())
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

        UserInfo recentUser = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("등록된 회원의 정보가 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글이 존재하지 않습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글이 존재하지 않습니다."));

        //만약 대댓글이 없으면 바로 삭제를 위해 deleted값을 Y로 변경
        //대댓글이 있으면 content의 값을 "작성자가 삭제한 댓글입니다."로 변경
        if (recentComment.getCmAnswerNum() == 0) {
            commentRepository.updateCmDeleted(cm_id);
        } else {
            commentRepository.updateCmContent(cm_id);
        }
        return recentComment.getCmId();
    }

    //개별 댓글 조회
    @Override
    public ResCommentListDto writeComments(Long cm_id) {

        Board_Comment comments = commentRepository.findByCmId(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

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
    @Override
    public Long answerWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id) {

        if (userRepository.findById(userInfo.getId()).isPresent()) {

            if (boardRepository.findById(bd_id).isPresent()) {

                if (commentRepository.findById(cm_id).isPresent()) {

                    //Long값의 댓글 번호를 int로 형변환
                    final int parentNum = cm_id.intValue();

                    UserInfo recentUserInfo = userRepository.findById(userInfo.getId()).get();

                    Board recentBoard = boardRepository.findById(bd_id).get();

                    Board_Comment board_Comment = commentRepository.findById(cm_id).get();

                    int depthCount = commentRepository.maxDepthValue(board_Comment.getCmGroup());

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

                    //댓글의 AnswerNum 증가
                    commentRepository.updateAnswerNum(cm_id);

                    return board_Comment.getCmId();
                }
            }
        }
        return -1L;
    }

    //대댓글 수정
    @Override
    public Long answerEdit(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id, Long an_id) {

        int parentValue = cm_id.intValue();

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("유저 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글이 존재하지 않습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글이 존재하지 않습니다."));

        Board_Comment recentAnswer = commentRepository.findByCmParentNumAndCmId(parentValue, an_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("대댓글이 존재하지 않습니다."));


        commentRepository.save(
                recentAnswer = Board_Comment.builder()
                        .cmId(recentAnswer.getCmId())
                        .cmContent(reqCommentDto.getCmContent())
                        .cmWriter(recentUserInfo.getUserid())
                        .cmDeleted(recentAnswer.getCmDeleted())
                        .cmCreated(recentAnswer.getCmCreated())
                        .cmModified(recentAnswer.getCmModified())
                        .cmGroup(recentAnswer.getCmGroup())
                        .cmDepth(recentAnswer.getCmDepth())
                        .cmParentNum(recentAnswer.getCmParentNum())
                        .cmStep(recentAnswer.getCmStep())
                        .userInfo(recentUserInfo)
                        .board(recentBoard)
                        .build()
        );

        return recentAnswer.getCmId();
    }

    //대댓글 삭제 상태 여부 변경
    @Override
    public Long answerDelete(UserInfo userInfo, Long bd_id, Long cm_id, Long an_id) {

        int parentValue = cm_id.intValue();

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("유저 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글이 존재하지 않습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글이 존재하지 않습니다."));

        Board_Comment recentAnswer = commentRepository.findByCmParentNumAndCmId(parentValue, an_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("대댓글이 존재하지 않습니다."));

        //대댓글 상태 변경
        commentRepository.updateAnDeleted(an_id);

        //댓글의 대댓글 갯수 -1
        commentRepository.deleteAnswerNum(cm_id);

        return recentAnswer.getCmId();
    }
}
