package com.example.book.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Data
// @Getter
// @Setter
// @ToString

public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private int price;
    private String author;
    // private LocalDateTime createDateTime;
    // private LocalDateTime updateDateTime;

}
