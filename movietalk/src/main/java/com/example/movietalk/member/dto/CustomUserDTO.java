package com.example.movietalk.member.dto;

import com.example.movietalk.member.entity.constant.Role;

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

    private String email;
    private String password;
    private String nickname;

    private Role role;

}
