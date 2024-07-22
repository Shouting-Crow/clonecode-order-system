package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.ItemType;
import com.clonecode.orderweb.domain.Seller;
import com.clonecode.orderweb.dto.ItemRegisterDto;
import com.clonecode.orderweb.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final String uploadDir = "src/main/resources/static/item-images/";

    @GetMapping("/item/register")
    public String showItemRegisterForm(HttpSession session, Model model){
        Object loginMember = session.getAttribute("loginMember");
        if (!(loginMember instanceof Seller)){
            return "redirect:/";
        }
        model.addAttribute("itemRegisterDto", new ItemRegisterDto());

        Map<String, String> itemTypes = Arrays.stream(ItemType.values())
                        .collect(Collectors.toMap(Enum::name, ItemType::getKoreanName));

        model.addAttribute("itemTypes", itemTypes);
        return "item/register";
    }

    @PostMapping("/item/register")
    public String registerItem(@ModelAttribute(name = "itemRegisterDto") ItemRegisterDto itemRegisterDto,
                               RedirectAttributes redirectAttributes,
                               HttpSession session){

        Object loginMember = session.getAttribute("loginMember");
        if (loginMember instanceof Seller seller){
            itemRegisterDto.setSellerId(seller.getId());

            if (!itemRegisterDto.getThumbnailImage().isEmpty()){
                String thumbnailImageUrl = saveImage(itemRegisterDto.getThumbnailImage());
                itemRegisterDto.setThumbnailImageUrl(thumbnailImageUrl);
            }

            for (MultipartFile detailImage : itemRegisterDto.getDetailImages()) {
                if (!detailImage.isEmpty()){
                    String detailImageUrl = saveImage(detailImage);
                    itemRegisterDto.addDetailImageUrl(detailImageUrl);
                }
            }

            itemService.registerItem(itemRegisterDto);
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 등록되었습니다.");
            return "redirect:/item/register-list";
        }
        return "redirect:/";
    }

    @GetMapping("/item/register-list")
    public String viewSellerList(HttpSession session, Model model){
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember instanceof Seller seller){
            List<Item> items = itemService.findSellerItem(seller.getId());
            model.addAttribute("items", items);
            return "item/register-list";
        }
        return "redirect:/";
    }

    private String saveImage(MultipartFile image){
        try {
            //파일명 -> UUID
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFileName);
            String savedFileName = UUID.randomUUID().toString() + "." + fileExtension;

            //파일 저장 경로 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            //파일 저장
            Path filePath = uploadPath.resolve(savedFileName);
            Files.copy(image.getInputStream(), filePath);

            //파일 URL 변환
            return "/item-images/" + savedFileName;
        } catch (IOException e){
            throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }

    private String getFileExtension(String fileName){
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

}
