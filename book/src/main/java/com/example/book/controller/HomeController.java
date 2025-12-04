package com.example.book.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.book.dto.BookDTO;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        log.info("Home 호출");
        return "home";
    }

}
