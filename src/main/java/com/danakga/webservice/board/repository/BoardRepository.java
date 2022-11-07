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

public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시판 목록 페이징
    Page<Board> findAllByBdDeletedAndBdType(String deleted, String BoardType, Pageable pageable);


    //작성한 게시글 조회
    Page<Board> findAllByUserInfoAndBdType(UserInfo userInfo, String boardType, Pageable pageable);


    //조회수 증가
    @Transactional
    @Modifying
    @Query("update Board b set b.bdViews = b.bdViews + 1 where b.bdId = :bdId")
    void updateView(@Param("bdId") Long bd_id);


    //게시글 삭제 여부 변경
    @Transactional
    @Modifying
    @Query("update Board b set b.bdDeleted = 'Y' where b.bdId = :bdId")
    void updateBdDeleted(@Param("bdId") Long bd_id);


    //전체 기준 게시판 검색
    @Query(
            value = "Select b from Board b "
                    + "where (b.bdTitle Like %:content% "
                    + "or b.bdContent Like %:content% "
                    + "or b.bdWriter Like %:content%) "
                    + "and b.bdType = :type and b.bdDeleted = :deleted"
    )
    Page<Board> searchBoard(@Param("content") String content,
                            @Param("deleted") String deleted,
                            @Param("type") String boardType,
                            Pageable pageable);


    //제목으로 게시판 검색
    @Query(
            value = "select b "
                    + "from Board b "
                    + "where b.bdTitle Like %:title% and b.bdDeleted = :deleted and b.bdType = :type"
    )
    Page<Board> SearchBoardTitle(@Param("deleted") String deleted,
                                 @Param("title") String content,
                                 @Param("type") String boardType,
                                 Pageable pageable);


    //작성자로 게시판 검색
    @Query(
            value = "select b "
                    + "from Board b "
                    + "where b.bdWriter = :writer and b.bdDeleted = :deleted and b.bdType = :type"
    )
    Page<Board> SearchBoardWriter(@Param("deleted") String deleted,
                                  @Param("writer") String content,
                                  @Param("type") String boardType,
                                  Pageable pageable);


    //내용으로 게시판 검색
    @Query(
            value = "select b "
                    + "from Board b "
                    + "where b.bdContent Like %:content% and b.bdDeleted = :deleted and b.bdType = :type"
    )
    Page<Board> SearchBoardContent(@Param("deleted") String deleted,
                                   @Param("content") String content,
                                   @Param("type") String boardType,
                                   Pageable pageable);


    /* ================ 마이페이지 ================ */

    //작성한 댓글의 게시글 조회
    @Query(
            value = "select b "
                    + "from Board b inner join Board_Comment bc on b.bdId = bc.board.bdId "
                    + "where bc.cmWriter = :cmWriter and b.bdType = :bdType and b.bdDeleted = :bdDeleted"
    )
    Page<Board> myCommentsPost(@Param("cmWriter") String cmWriter,
                               @Param("bdType") String boardType,
                               @Param("bdDeleted") String bdDeleted,
                               Pageable pageable);

    /* ================ 관리자(admin) ================ */
    Page<Board> findByBdDeleted(String sort, Pageable pageable);

}
