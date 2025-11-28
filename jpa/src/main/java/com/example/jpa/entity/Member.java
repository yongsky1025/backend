package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.jpa.entity.constant.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString

@EntityListeners(value = AuditingEntityListener.class)
@Table(name = "memberTbl")
@Entity
public class Member {
    //  아이디(필수), 이름(필수), 역할(MEMBER, ADMIN) 가입일자, 수정일자, 자기소개

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @CreatedDate
    private LocalDateTime createDate;
    
    @LastModifiedDate
    private LocalDateTime updateDate;

    // CLOB 지정한다면 @Lob
    @Column(length = 2000)
    // @Lob
    private String description;


    // 메서드
    public void changeRole(RoleType role){
        this.role = role;
    }

}