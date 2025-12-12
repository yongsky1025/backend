package com.example.board.post.dto;

import java.time.LocalDateTime;

import com.example.board.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    private String writerEmail; // 작성자 이메일
    private String writerName; // 작성자 이름

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private int replyCnt;

}
