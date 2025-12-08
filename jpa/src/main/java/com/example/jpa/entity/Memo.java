package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

// BaseEntity에서 상속받음
// @EntityListeners(value = AuditingEntityListener.class)
// // main => @EnableJpaAuditing
@Entity
@Table(name = "memotbl")
public class Memo extends BaseEntity {
    // 테이블(memotbl)데이터베이스 컬럼 : mno, memo_text, create_date, update_date
    // 클래스 필드명 == 테이블 컬럼명
    // 클래스 필드명 != 테이블 컬럼명(@Column(name=""))

    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 하나씩 증가(auto increment)
    @Id // Entity사용할때 무조건, PK
    @Column(name = "mno")
    private Long id;

    @Column(nullable = false, name = "momo_text")
    private String text; //

    // BaseEntity에서 상속받음
    // @CreatedDate
    // private LocalDateTime createDate;

    // @LastModifiedDate
    // private LocalDateTime updateDate;

    // text 수정 메소드
    public void changeText(String text) {
        this.text = text;
    }

}