package com.example.movietalk.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movietalk.member.dto.AuthUserDTO;
import com.example.movietalk.member.dto.CustomUserDTO;
import com.example.movietalk.member.dto.PasswordDTO;
import com.example.movietalk.member.entity.constant.Role;
import com.example.movietalk.member.service.CustomUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
@Controller
public class MemberController {

    private final CustomUserService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/leave")
    public void getLeave() {
        log.info("회원탈퇴 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/leave")
    public String postLeave(CustomUserDTO dto, HttpSession session, RedirectAttributes rttr) {
        log.info("회원탈퇴 요청");

        try {
            service.leave(dto);
            session.invalidate(); // 현재 로그인 정보 제거
        } catch (Exception e) {
            log.info(e.getMessage());
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/leave";
        }

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public void getProfile() {
        log.info("profile 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public void getEdit() {
        log.info("edit 폼 요청");
    }

    // 닉네임 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/nickname")
    public String postNickname(CustomUserDTO dto) {
        log.info("닉네임 변경 요청{}", dto);

        service.changeNickName(dto);
        // SecurityContext 정보 업데이트
        Authentication authentication = getAuthentication();
        AuthUserDTO auth = (AuthUserDTO) authentication.getPrincipal();
        auth.getCustomUserDTO().setNickname(dto.getNickname());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    // 비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/password")
    public String postPwd(PasswordDTO dto, HttpSession session, RedirectAttributes rttr) {
        log.info("비밀번호 변경 {}", dto);

        try {
            service.changePassword(dto);
            session.invalidate(); // 현재 로그인 정보 제거
        } catch (Exception e) {
            log.info(e.getMessage());
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/edit";
        }

        return "redirect:/member/login";
    }

    // login + GET
    @GetMapping("login")
    public void getLogin() {
        log.info("로그인 폼 요청");
    }

    @GetMapping("/register")
    public void getRegister(CustomUserDTO customUserDTO) {
        log.info("회원가입 폼 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid CustomUserDTO customUserDTO, BindingResult result) {
        log.info("회원가입 요청{}", customUserDTO);

        if (result.hasErrors()) {
            return "/member/register";
        }

        // 서비스 작업
        try {
            customUserDTO.setRole(Role.MEMEBER);
            Long mid = service.join(customUserDTO);
            return "redirect:/member/login";
        } catch (Exception e) {
            log.info(e.getMessage());
            return "/member/register";
        }
    }

    private Authentication getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication;

    }

    // 로그인 이후 SecurityContext 에 담긴 정보 확인(개발용)
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAthenticationInfo() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }

}
