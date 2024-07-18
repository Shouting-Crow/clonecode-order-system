package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.dto.LoginDto;
import com.clonecode.orderweb.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String showLoginForm(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "login/login";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto,
                        Model model,
                        HttpSession httpSession){

        Object member = loginService.login(loginDto.getLoginId(), loginDto.getPassword());
        if (member != null){
            httpSession.setAttribute("loginMember", member);
            return "redirect:/";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }

}
