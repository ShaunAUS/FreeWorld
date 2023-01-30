package com.example.testSecurity.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FrontTestController {

    @GetMapping("")
    public String getTestLanding() {
        System.out.println("index Page 호출");
        return "index";
    }
}
