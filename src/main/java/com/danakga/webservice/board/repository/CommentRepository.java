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
    @Query("update Board_Comment cm set cm.cmDeleted = 'Y' where cm.cmId = :id")
    void updateDeleted(@Param("id") Long id);

    //회원이 작성한 댓글 총 페이지 수, 총 갯수
    @Query(
            value = "select bc "
                    + "from Board_Comment bc inner join Board b on bc.board.bdId = b.bdId "
                    + "where bc.cmWriter = :cmWriter and b.bdType = :bdType and bc.cmDeleted = :cmDeleted"
    )
    Page<Board_Comment> myCommentsList(@Param("cmWriter") String cmWriter,
                                       @Param("bdType") String boardType,
                                       @Param("cmDeleted") String cmDeleted,
                                       Pageable pageable);
}
