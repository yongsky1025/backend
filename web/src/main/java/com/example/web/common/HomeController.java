package com.example.web.common;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Log4j2
public class HomeController {

    @GetMapping("/")
    public String getHome() {
    log.info("home으로 리턴");        
        return "home";
    }
    
    @GetMapping("/separate")
    public void getSparate() {
        log.info("separate 호출");
    }
    
    
}
