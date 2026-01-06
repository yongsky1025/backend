package com.example.movietalk.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class commonException {

    // 존재하지 않는 URL => /error 내부 포워딩
    @ExceptionHandler(NoResourceFoundException.class)
    public String getError() {
        log.info("404에러 처리");
        return "/except/url404";
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e, Model model) {
        log.info("500에러 처리");
        model.addAttribute("e", e);
        return "/except/500";
    }

}
