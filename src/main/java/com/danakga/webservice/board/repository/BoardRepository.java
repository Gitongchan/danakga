package com.danakga.webservice.board.repository;

import com.danakga.webservice.board.model.Board;
import com.danakga.webservice.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시글 아이디 조회
    Optional<Board> findByBdId(Long id);

    //게시판 목록 페이징
    Page<Board> findAllByBdDeletedAndBdType(String deleted, String BoardType, Pageable pageable);
    
    //작성한 게시글 조회
    Page<Board> findAllByUserInfoAndBdType(UserInfo userInfo, String boardType, Pageable pageable);

    //조회수 증가
    @Transactional
    @Modifying
    @Query("update Board b set b.bdViews = b.bdViews + 1 where b.bdId = :id")
    void updateView(@Param("id") Long id);

    //게시글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board b set b.bdDeleted = 'Y' where b.bdId = :id")
    void updateDeleted(@Param("id") Long id);

    //작성한 댓글의 게시글 조회
    //런타임 오류있고, inner join으로 하려면 @ManyToOne(optional = false)로 하면 된다고는 함
//    @Query(
//            value = "select b.bdId, b.bdTitle, b.bdCreated, b.bdWriter, b.bdViews " +
//                    "from Board b inner join Board_Comment bc on b.bdId = bc.board.bdId " +
//                    "where bc.cmWriter = :writer and b.bdType = :boardType and b.bdDeleted = :deleted "
//    )
//    Page<Board> myCommentsList(@Param("cm_writer") String writer,
//                               @Param("bd_type") String boardType,
//                               @Param("bd_deleted") String deleted,
//                               Pageable pageable);

    //런타임 오류는 없는데 포스트맨 결과 값 안뜸
    @Query(
            "select b from Board b " +
            "where b = (select bc.board from Board_Comment bc join bc.board where bc.userInfo = :userInfo)"
    )
    Page<Board> myCommentsList(@Param("userInfo") UserInfo userInfo,
                               Pageable pageable);

}
