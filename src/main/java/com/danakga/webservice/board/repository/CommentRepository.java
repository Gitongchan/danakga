package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.board.model.Board_Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Board_Comment, Long> {

    //댓글 수정 시 bd_id, cm_id에 맞는 댓글만 수정
    Board_Comment findByBoardAndCmId(Board board, Long cm_id);

    //개별 댓글 조회
    Optional<Board_Comment> findByCmId(Long cm_id);

    //댓글 조회
    @Query(
            value = "select bc " +
                    "from Board_Comment bc " +
                    "where bc.board.bdId = :bdId and bc.cmDeleted = :cmDeleted and bc.cmStep = :cmStep " +
                    "order by bc.cmGroup desc, bc.cmDepth asc"
    )
    Page<Board_Comment> answerList(@Param("bdId") Long bd_id,
                                    @Param("cmStep") int cmStep,
                                    @Param("cmDeleted") String deleted,
                                    Pageable pageable);

    Page<Board_Comment> findAllByBoardAndCmDeletedAndCmStep(Board board, String deleted, int commentStep, Pageable pageable);

    /*  댓글과 대댓글 정렬 조회 방법
    (위 조건으로 where절 추가 후 뿌리기)
    SELECT *
    FROM board_comment
    ORDER BY cm_group DESC, cm_depth ASC
    */

    //댓글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.cmId = :cmId")
    void updateCmDeleted(@Param("cmId") Long cm_id);

    //회원이 작성한 댓글 조회
    @Query(
            value = "select bc " +
                    "from Board_Comment bc inner join Board b on bc.board.bdId = b.bdId " +
                    "where bc.cmWriter = :cmWriter and b.bdType = :bdType and bc.cmDeleted = :cmDeleted"
    )
    Page<Board_Comment> myCommentsList(@Param("cmWriter") String cmWriter,
                                       @Param("bdType") String boardType,
                                       @Param("cmDeleted") String cmDeleted,
                                       Pageable pageable);

    // 그룹의 최댓값
    @Query(
            value = "select max(bc.cmGroup) as maxGroup "
                    + "from Board_Comment bc"
    )
    Integer maxGroupValue();

    // group에서의 depth의 최댓값
    @Query(
            value = "select max(bc.cmDepth) as maxDepth " +
                    "from Board_Comment bc " +
                    "where bc.cmGroup = :cmGroup"
    )
    int maxDepthValue(@Param("cmGroup") int cmGroup);


    // answerNum(대댓글 갯수) 증가
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmAnswerNum = bc.cmAnswerNum + 1 where bc.cmId = :cmId")
    void updateAnswerNum(@Param("cmId") Long cm_id);

    // answerNum(대댓글 갯수) 증가
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmAnswerNum = bc.cmAnswerNum - 1 where bc.cmId = :cmId")
    void deleteAnswerNum(@Param("cmId") Long cm_id);


    //대댓글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.cmId = :anId")
    void updateAnDeleted(@Param("anId") Long an_id);
}
