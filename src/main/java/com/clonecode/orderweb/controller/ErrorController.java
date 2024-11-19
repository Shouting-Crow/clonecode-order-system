package com.clonecode.orderweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "이 페이지는 구매자 회원만 접근 가능합니다. 로그인 후 이용해 주시길 바랍니다.");
        return "error/access-denied";
    }
}
