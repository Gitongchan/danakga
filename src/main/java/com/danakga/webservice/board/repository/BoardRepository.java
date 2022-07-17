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

import java.util.List;
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
                    + "where b.bdWriter Like %:writer% and b.bdDeleted = :deleted and b.bdType = :type"
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

    //작성한 댓글의 게시글 조회
    @Query(
            value = "select b "
                    + "from Board b inner join Board_Comment bc on b.bdId = bc.board.bdId "
                    + "where bc.cmWriter = :cmWriter and b.bdType = :bdType and b.bdDeleted = :bdDeleted"
    )
    List<Board> myCommentsPost(@Param("cmWriter") String cmWriter,
                               @Param("bdType") String boardType,
                               @Param("bdDeleted") String bdDeleted);
}
