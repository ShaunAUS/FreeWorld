package com.example.testSecurity.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/v1")  이거쓰면 404뜸... 왜지?
public class FrontTestController {

    @GetMapping("/basic")
    public String basic(){
        return "basic";
    }
    @GetMapping("/test")
    public String login(){
        return "member";
    }
}
