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
    Page<Board_Comment> findAllByBoardAndCmDeleted(Board board, String deleted, Pageable pageable);

    //댓글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.cmId = :cmId")
    void updateDeleted(@Param("cmId") Long cm_id);

    //회원이 작성한 댓글 조회
    @Query(
            value = "select bc "
                    + "from Board_Comment bc inner join Board b on bc.board.bdId = b.bdId "
                    + "where bc.cmWriter = :cmWriter and b.bdType = :bdType and bc.cmDeleted = :cmDeleted"
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

    // depth의 최댓값
    @Query(
            value = "select max(bc.cmDepth) as maxDepth "
                    + "from Board_Comment bc "
                    + "where bc.cmGroup = :cmGroup"
    )
    Integer maxDepthValue(@Param("cmGroup") int cmGroup);

    // answerNum(대댓글 갯수) 증가
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmAnswerNum = bc.cmAnswerNum + 1 where bc.cmId = :cmId")
    void updateAnswerNum(@Param("cmId") Long cm_id);
}
