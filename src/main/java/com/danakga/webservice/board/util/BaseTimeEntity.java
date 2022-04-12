package com.danakga.webservice.board.util;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // entity 클래스가 해당 클래스를 상속받을 경우 이 클래스의 필드도 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    //entity가 생성되어 저장될 때 까지의 시간 자동 저장
    @CreatedDate
    private LocalDateTime bd_created;

    // 조회한 entity의 값을 변경할 때 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime bd_modified;
}
