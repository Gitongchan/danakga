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

    /* =============== 댓글 =============== */

    //댓글 수정 시 bd_id, cm_id에 맞는 댓글만 수정
    Optional<Board_Comment> findByBoardAndCmId(Board board, Long cm_id);


    //개별 댓글 조회
    Optional<Board_Comment> findByCmId(Long cm_id);


    //댓글 조회
    @Query(
            value = "select bc " +
                    "from Board_Comment  bc " +
                    "where bc.board.bdId = :bdId and bc.cmStep = :cmStep and bc.cmDeleted = :cmDeleted1 or bc.cmDeleted = :cmDeleted2 "
    )
    Page<Board_Comment> commentList(@Param("bdId") Long bd_id,
                                    @Param("cmDeleted1") String cm_deleted1,
                                    @Param("cmDeleted2") String cm_deleted2,
                                    @Param("cmStep") int commentStep,
                                    Pageable pageable);


    //대댓글이 없는 경우 댓글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.cmId = :cmId")
    void updateCmDeleted(@Param("cmId") Long cm_id);


    //대댓글이 있는 경우 댓글 삭제 시 content 값 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'M', bc.cmContent = '작성자가 삭제한 댓글 입니다.' where bc.cmId = :cmId")
    void updateCmContent(@Param("cmId") Long cm_id);


    /* =============== 대댓글 =============== */
    
    // answerNum(대댓글 갯수) 증가
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmAnswerNum = bc.cmAnswerNum + 1 where bc.cmId = :cmId")
    void updateAnswerNum(@Param("cmId") Long cm_id);


    //대댓글 체크
    Optional<Board_Comment> findByCmParentNumAndCmId(int cm_id, Long an_id);


    //대댓글 조회
    @Query(
            value = "select bc " +
                    "from Board_Comment bc " +
                    "where bc.cmParentNum = :cmParentNum and bc.cmDeleted = :cmDeleted and bc.cmStep = :cmStep " +
                    "order by bc.cmGroup desc, bc.cmDepth asc"
    )
    Page<Board_Comment> answerList(@Param("cmParentNum") int cmParentNum,
                                   @Param("cmDeleted") String deleted,
                                   @Param("cmStep") int cmStep,
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


    // answerNum(대댓글 갯수) 감소
    // 대댓글 값으로 댓글도 같이 삭제 해야하기 때문에 사용
    // clearAutomatically 사용하여 1차 캐시(jpa의 가상 db라고 할 수 있음)와 db 동기화
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Board_Comment bc set bc.cmAnswerNum = bc.cmAnswerNum - 1 where bc.cmId = :cmId")
    void deleteAnswerNum(@Param("cmId") Long cm_id);


    //대댓글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.cmId = :anId")
    void updateAnDeleted(@Param("anId") Long an_id);


    // 게시글 삭제 시 작성된 댓글, 대댓글도 같이 삭제 상태 변경
    @Transactional
    @Modifying
    @Query("update Board_Comment bc set bc.cmDeleted = 'Y' where bc.board.bdId = :bdId")
    void deleteBoardComment(@Param("bdId") Long bd_id);


    /* =============== 마이페이지 =============== */

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
}
