package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.dto.MemberRegisterDto;
import com.clonecode.orderweb.service.MemberRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberRegisterController {

    private final MemberRegisterService memberRegisterService;

    @GetMapping("/register/customer")
    public String showCustomerRegisterForm(Model model){
        model.addAttribute("customerDto", new MemberRegisterDto());
        return "register/customer";
    }

    @GetMapping("/register/seller")
    public String showSellerRegisterForm(Model model){
        model.addAttribute("sellerDto", new MemberRegisterDto());
        return "register/seller";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @ModelAttribute(name = "customerDto") MemberRegisterDto customerDto,
                                   BindingResult bindingResult,
                                   Model model){
        if (bindingResult.hasErrors()){
            return "register/customer";
        }

        try{
            memberRegisterService.registerCustomer(customerDto);
            model.addAttribute("message", "구매자 회원 등록이 완료되었습니다.");
            return "home";
        } catch (IllegalArgumentException e){
            bindingResult.rejectValue("loginId", "error.customerDto", e.getMessage());
            return "register/customer";
        }
    }

    @PostMapping("/register/seller")
    public String registerSeller(@Valid @ModelAttribute(name = "sellerDto") MemberRegisterDto sellerDto,
                                   BindingResult bindingResult,
                                   Model model){
        if (bindingResult.hasErrors()){
            return "register/seller";
        }

        try{
            memberRegisterService.registerSeller(sellerDto);
            model.addAttribute("message", "판매자 회원 등록이 완료되었습니다.");
            return "home";
        } catch (IllegalArgumentException e){
            bindingResult.rejectValue("loginId", "error.sellerDto", e.getMessage());
            return "register/seller";
        }
    }

}
