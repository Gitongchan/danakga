package com.danakga.webservice.shopqna.repository;

import com.danakga.webservice.shopqna.model.ShopQnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ShopQnARepository extends JpaRepository<ShopQnA, Long> {

    /* 문의 사항 삭제 여부 변경 */
    @Transactional
    @Modifying
    @Query("update ShopQnA sq set sq.sqDeleted = 'Y' where sq.sqId = :sqId")
    void updateInDeleted(@Param("sqId") Long sq_id);
    
    /* 문의 사항 목록 조회 */
    Page<ShopQnA> findAllBySqDeleted(Pageable pageable, String deleted);

}
