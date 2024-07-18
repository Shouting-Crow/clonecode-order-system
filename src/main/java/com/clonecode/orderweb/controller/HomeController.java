package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Seller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String viewHome(HttpSession session, Model model){
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember == null){
            return "home";
        } else if (loginMember instanceof Seller seller){
            model.addAttribute("loginMember", seller);
            return "loginHome";
        } else if (loginMember instanceof Customer customer){
            model.addAttribute("loginMember", customer);
            return "loginHome";
        }
        return "home";
    }

}
