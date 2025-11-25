package com.example.web.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Log4j2
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list")
    public void getList() {
        log.info("/board/list 호출");
    }
    
    
}
