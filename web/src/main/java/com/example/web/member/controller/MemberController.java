package com.example.web.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web.member.dto.LoginDTO;
import com.example.web.member.dto.RegisterDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@Log4j2
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    public void getLogin() {
        log.info("/member/login 요청");
    }

    // HttpSession : http 프로토콜 단점 해결
    // 로그인, 장바구니
    // 서버 쪽에 정보 저장

    // 브라우저 정보 저장
    // cookie

    @PostMapping("login")
    public String postLogin(LoginDTO dto, HttpSession session) {
        // id, password 가져오기
        log.info("로그인 post {}",dto);
        session.setAttribute("loginDto", dto);
        
        return "redirect:/";
    }
    
    
    @GetMapping("/register")
    public void getRegister(RegisterDTO dto) {
        log.info("/member/register 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid RegisterDTO dto, BindingResult result) {
        // id, password, email
        log.info("회원가입 post {}", dto);

        if (result.hasErrors()) {
            return "/member/register";
        }
        
        return "redirect:/member/login";
    }
    
}
