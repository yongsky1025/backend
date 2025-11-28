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


@EntityListeners(value = AuditingEntityListener.class)
@Table(name="boardtbl")
@Entity
public class Board {
    // id(자동 순번), 제목(title), 내용(content-1500)not null, 작성자(writer-20)not null
    // 작성일, 수정일

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    @Column(nullable = false, length = 1500)
    private String content;
    
    @Column(nullable = false, length = 20)
    private String writer;

    @CreatedDate
    private LocalDateTime createDateTime;
    
    @LastModifiedDate
    private LocalDateTime updateDateTime;


    // 메서드
    // 보드 수정
    public void changeContent(String content){
        this.content = content;
    }
    public void changeTitle(String title){
        this.title = title;

    }
}
