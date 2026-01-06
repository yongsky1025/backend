package com.example.movietalk.member.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.member.dto.AuthUserDTO;
import com.example.movietalk.member.dto.CustomUserDTO;
import com.example.movietalk.member.dto.PasswordDTO;
import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.repository.MemberRepository;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@RequiredArgsConstructor
@Log4j2
@Service
public class CustomUserService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원탈퇴
    public void leave(CustomUserDTO dto) throws IllegalStateException {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

        // 현재 비밀번호가 맞는지 확인
        if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            reviewRepository.deleteByMember(member);
            memberRepository.delete(member);
        } else {
            throw new IllegalStateException("현재 비밀번호가 다릅니다");
        }
    }

    // 비밀번호 변경
    public void changePassword(PasswordDTO dto) throws IllegalStateException {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

        // 현재 비밀번호가 맞는지 확인
        if (passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            member.changePassword(passwordEncoder.encode(dto.getNewPassword()));
        } else {
            throw new IllegalStateException("현재 비밀번호가 다릅니다");
        }

    }

    // 닉네임 변경
    public void changeNickName(CustomUserDTO dto) {
        // 대상찾기 => 수정 내용 처리
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));
        member.changeNickname(dto.getNickname());

    }

    // 회원가입
    public Long join(CustomUserDTO dto) {

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .role(dto.getRole())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        return memberRepository.save(member).getMid();
    }

    // 기본 로그인 작업
    // 아이디/비밀번호 폼에서 입력받기 => 컨트롤러 => 서비스 => 아이디와 비밀번호가 일치하는 회원 존재여부 확인
    // => 존재한다면 회원 정보를 session 객체에 담은 후 전체 사이트에서 정보를 유지
    // 로그아웃 => 세션 정보 제거

    // 시큐리티 로그인 작업
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("로그인 요청 {}", email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원정보를 찾을 수 없습니다."));

        // entity => dto
        CustomUserDTO customUserDTO = CustomUserDTO.builder()
                .mid(member.getMid())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();

        AuthUserDTO authUserDTO = new AuthUserDTO(customUserDTO);

        // return User.builder()
        // .username(member.getEmail())
        // .password(member.getPassword())
        // .build();

        return authUserDTO;
    }

}
