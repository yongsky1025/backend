package com.example.memo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class MemoDTO {
    private Long id;
    private String text;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
