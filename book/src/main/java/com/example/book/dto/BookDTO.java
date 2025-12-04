package com.example.book.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;

    @NotBlank(message = "타이틀은 필수입니다.")
    private String title;

    @Max(value = 10000000, message = "가격은 10000000 이하여야 합니다.")
    @Min(value = 0, message = "가격은 최소 0 이상이어야 합니다.")
    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @NotBlank(message = "저자는 필수 요소입니다.")
    private String author;

    private String description;
    // private LocalDateTime createDateTime;
    // private LocalDateTime updateDateTime;

}
