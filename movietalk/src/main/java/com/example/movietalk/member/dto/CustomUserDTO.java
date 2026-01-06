package com.example.movietalk.member.dto;

import com.example.movietalk.member.entity.constant.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomUserDTO {

    private Long mid;

    @Email(message = "이메일 형식을 확인해 주세요")
    @NotBlank(message = "필수 입력 요소입니다.")
    private String email;
    @NotBlank(message = "필수 입력 요소입니다.")
    private String password;
    @NotBlank(message = "필수 입력 요소입니다.")
    private String nickname;

    private Role role;

}
