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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //댓글 조회
    @Override
    public ResCommentListDto commentsList(Long bd_id, Pageable pageable, int page) {

        //삭제여부가 "N", "M"의 값만 가져오기 위한 변수
        final String deleted1 = "N";
        final String deleted2 = "M";

        //댓글, 대댓글 구분
        final int commentStep = 0;
        final int answerStep = 1;

        //해당 bd_id값이 없다면 exception 전달
        Board board = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        //step이 0값 == 댓글 조회
        pageable = PageRequest.of(page, 10, Sort.by("cmCreated").descending());
        Page<Board_Comment> comments = commentRepository.commentList(bd_id, deleted1, deleted2, commentStep, pageable);

        //Dto에 값을 담아주기 위한 List<Map>
        List<Map<String, Object>> commentList = new ArrayList<>();

        //대댓글 담을 List<map>
        List<Map<String, Object>> answersList = new ArrayList<>();

        //댓글 담을 map (LinkedHashMap은 키값의 순서를 보장하기 위한 HashMap)
        Map<String, Object> commentsMap = new LinkedHashMap<>();

        //List 값을 반복문으로 Map에 담아줌
        comments.forEach(comment -> {

            int parentNum = comment.getCmId().intValue();

            //DB를 조회할때 forEach로 반복되는 댓글의 id값을 받아서 각 댓글의 대댓글을 조회
            Pageable answerPage = PageRequest.of(page, 10);
            Page<Board_Comment> answers = commentRepository.answerList(parentNum, deleted1, answerStep, answerPage);

            answers.forEach(answer -> {

                //대댓글 값 담는 Map
                Map<String, Object> answersMap = new HashMap<>();

                answersMap.put("cm_id", answer.getCmId());
                answersMap.put("cm_content", answer.getCmContent());
                answersMap.put("cm_writer", answer.getCmWriter());
                answersMap.put("cm_step", answer.getCmStep());
                answersMap.put("cm_parentNum", answer.getCmParentNum());
                answersMap.put("cm_deleted", answer.getCmDeleted());
                answersMap.put("cm_created", answer.getCmCreated());
                answersMap.put("cm_modify", answer.getCmModified());
                answersList.add(answersMap);
            });

            commentsMap.put("cm_id", comment.getCmId());
            commentsMap.put("cm_content", comment.getCmContent());
            commentsMap.put("cm_writer", comment.getCmWriter());
            commentsMap.put("cm_step", comment.getCmStep());
            commentsMap.put("cm_deleted", comment.getCmDeleted());
            commentsMap.put("cm_created", comment.getCmCreated());
            commentsMap.put("cm_modify", comment.getCmModified());
            commentsMap.put("cm_answerNum", comment.getCmAnswerNum());
            commentsMap.put("totalElement", comments.getTotalElements());
            commentsMap.put("totalPage", comments.getTotalPages());
            commentsMap.put("answer", answersList);
            commentList.add(commentsMap);
        });

        return new ResCommentListDto(commentList);
    }

    //댓글 작성
    @Override
    public ResResultDto commentsWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id) {


        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        // null값 비교를 위해 Integer 사용
        Integer groupCount = commentRepository.maxGroupValue();

        //댓글이 처음 작성 됐을 때를 위한 초기 값 설정
        if (groupCount == null) {
            groupCount = 0;
        }

        Board_Comment board_Comment = commentRepository.save(
                Board_Comment.builder()
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

        return new ResResultDto(board_Comment.getCmId(), "댓글을 작성 했습니다.");
    }

    //댓글 수정
    @Override
    public ResResultDto commentsEdit(Long bd_id, Long cm_id, UserInfo userInfo, ReqCommentDto reqCommentDto) {

        /* 회원, 게시글, 댓글 조회 */
        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment board_Comment = commentRepository.findByBoardAndCmId(recentBoard, cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        board_Comment = commentRepository.save(
                Board_Comment.builder()
                        .cmId(board_Comment.getCmId())
                        .cmContent(reqCommentDto.getCmContent())
                        .cmWriter(recentUserInfo.getUserid())
                        .cmDeleted(board_Comment.getCmDeleted())
                        .cmCreated(board_Comment.getCmCreated())
                        .userInfo(recentUserInfo)
                        .board(recentBoard)
                        .build()
        );

        return new ResResultDto(board_Comment.getCmId(), "댓글을 수정 했습니다.");
    }

    //댓글 삭제 여부 변경
    @Override
    public ResResultDto commentsDelete(Long bd_id, Long cm_id, UserInfo userInfo) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board checkBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment checkComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        //만약 대댓글이 없으면 바로 삭제를 위해 deleted값을 Y로 변경
        //대댓글이 있으면 content의 값을 "작성자가 삭제한 댓글입니다."로 변경
        if (checkComment.getCmAnswerNum() == 0) {
            commentRepository.updateCmDeleted(cm_id);
        } else {
            //대댓글이 있는 경우 상태 값 M으로 변경
            commentRepository.updateCmContent(cm_id);
        }

        return new ResResultDto(checkComment.getCmId(), "댓글을 삭제 했습니다.");
    }

    //개별 댓글 조회
    @Override
    public ResCommentListDto writeComments(Long cm_id) {

        final String deleted = "N";
        final int answerStep = 1;

        Board_Comment checkcomments = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        final int parentNum = checkcomments.getCmId().intValue();

        //개별 댓글 조회
        List<Board_Comment> answers = commentRepository.eachAnswerList(parentNum, deleted, answerStep);

        //Dto에 값을 담아주기 위한 List<Map>
        List<Map<String, Object>> commentList = new ArrayList<>();

        List<Map<String, Object>> answerList = new ArrayList<>();

        //map에 댓글 정보 put
        Map<String, Object> commentMap = new LinkedHashMap<>();

        Map<String, Object> answerMap = new HashMap<>();

        answers.forEach(answer -> {
            answerMap.put("cm_id", answer.getCmId());
            answerMap.put("cm_content", answer.getCmContent());
            answerMap.put("cm_writer", answer.getCmWriter());
            answerMap.put("cm_step", answer.getCmStep());
            answerMap.put("cm_parentNum", answer.getCmParentNum());
            answerMap.put("cm_deleted", answer.getCmDeleted());
            answerMap.put("cm_created", answer.getCmCreated());
            answerMap.put("cm_modify", answer.getCmModified());
            answerList.add(answerMap);
        });

        commentMap.put("cm_id", checkcomments.getCmId());
        commentMap.put("cm_comment", checkcomments.getCmContent());
        commentMap.put("cm_created", checkcomments.getCmCreated());
        commentMap.put("cm_deleted", checkcomments.getCmDeleted());
        commentMap.put("cm_modified", checkcomments.getCmModified());
        commentMap.put("cm_writer", checkcomments.getCmWriter());
        commentMap.put("answers", answerList);
        commentList.add(commentMap);

        return new ResCommentListDto(commentList);
    }

    /* =============== 대댓글 =============== */

    //대댓글 작성
    @Override
    public Long answerWrite(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id) {

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글 정보를 찾을 수 없습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        // int형 변환
        final int parentNum = cm_id.intValue();

        // depth 최대값
        final int depthCount = commentRepository.maxDepthValue(recentComment.getCmGroup());

        Board_Comment writeAnswer = commentRepository.save(
                Board_Comment.builder()
                        .cmContent(reqCommentDto.getCmContent())
                        .cmWriter(recentUserInfo.getUserid())
                        .cmGroup(recentComment.getCmGroup())
                        .cmStep(1)
                        .cmDepth(depthCount + 1)
                        .cmParentNum(parentNum)
                        .board(recentBoard)
                        .userInfo(recentUserInfo)
                        .build()
        );
        //댓글의 AnswerNum 증가
        commentRepository.updateAnswerNum(cm_id);

        return writeAnswer.getCmId();
    }

    //대댓글 수정
    @Override
    public Long answerEdit(UserInfo userInfo, ReqCommentDto reqCommentDto, Long bd_id, Long cm_id, Long an_id) {

        // int형 변환
        final int parentValue = cm_id.intValue();

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        Board_Comment recentAnswer = commentRepository.findByCmParentNumAndCmId(parentValue, an_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("대댓글을 찾을 수 없습니다."));

        recentAnswer = commentRepository.save(
                Board_Comment.builder()
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

        // int형 변환
        final int parentValue = cm_id.intValue();

        UserInfo recentUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("유저 정보를 찾을 수 없습니다."));

        Board recentBoard = boardRepository.findById(bd_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Board_Comment recentComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        Board_Comment recentAnswer = commentRepository.findByCmParentNumAndCmId(parentValue, an_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("대댓글을 찾을 수 없습니다."));

        //대댓글 상태 변경
        commentRepository.updateAnDeleted(an_id);

        //댓글의 대댓글 갯수 -1
        //여기서 1차 캐시와 db가 연동되어서 1차 캐시 값 최신화
        commentRepository.deleteAnswerNum(cm_id);

        /* update 후에 댓글 최신 값으로 조회 */
        Board_Comment checkComment = commentRepository.findById(cm_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        // 2가지 모두 참일때만 처리
        // 댓글이 삭제 처리지만 대댓글이 있는 경우
        if (checkComment.getCmDeleted().equals("M") && checkComment.getCmAnswerNum() == 0) {
            commentRepository.updateCmDeleted(cm_id);
        }

        return recentAnswer.getCmId();
    }
}
