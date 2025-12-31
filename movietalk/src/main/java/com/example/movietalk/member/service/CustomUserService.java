package com.example.movietalk.member.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class CustomUserService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 기본 로그인 작업
    // 아이디/비밀번호 폼에서 입력받기 => 컨트롤러 => 서비스 => 아이디와 비밀번호가 일치하는 회원 존재여부 확인
    // => 존재한다면 회원 정보를 session 객체에 담은 후 전체 사이트에서 정보를 유지
    // 로그아웃 => 세션 정보 제거

    // 시큐리티 로그인 작업
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())

                .build();

    }

}
