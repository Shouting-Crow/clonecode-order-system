package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.dto.ItemDetailDto;
import com.clonecode.orderweb.dto.ReviewRegisterDto;
import com.clonecode.orderweb.service.ItemService;
import com.clonecode.orderweb.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemService itemService;

    @GetMapping("/items/{itemId}/reviews/new")
    public String showReviewRegisterForm(@PathVariable long itemId,
                                         Model model,
                                         HttpSession httpSession) {
        Customer customer = (Customer) httpSession.getAttribute("loginMember");

        if (customer == null) {
            return "redirect:/login";
        }

        ReviewRegisterDto reviewRegisterDto = new ReviewRegisterDto();
        model.addAttribute("reviewRegisterDto", reviewRegisterDto);

        Optional<ItemDetailDto> itemDetail = itemService.getItemDetail(itemId, PageRequest.of(0, 5));
        itemDetail.ifPresent(item -> model.addAttribute("item", item));

        return "review/review-form";
    }


    @PostMapping("/items/{itemId}/reviews")
    public String registerReview(@PathVariable(name = "itemId") Long itemId,
                                 @ModelAttribute ReviewRegisterDto reviewRegisterDto,
                                 HttpSession session){
        Customer customer = (Customer) session.getAttribute("loginMember");

        if(customer == null){
            return "redirect:/login";
        }

        reviewService.registerReview(itemId, customer.getId(), reviewRegisterDto);

        return "redirect:/item/detail/" + itemId;
    }
}
