package com.clonecode.orderweb.controller;

import com.clonecode.orderweb.domain.Customer;
import com.clonecode.orderweb.domain.Item;
import com.clonecode.orderweb.domain.ItemType;
import com.clonecode.orderweb.domain.Seller;
import com.clonecode.orderweb.dto.*;
import com.clonecode.orderweb.service.ItemService;
import com.clonecode.orderweb.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
    private final ReviewService reviewService;
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

    @GetMapping("/item/edit/{id}")
    public String showItemEditForm(@PathVariable(name = "id") Long id,
                                   Model model,
                                   HttpSession session){
        Object loginMember = session.getAttribute("loginMember");
        if (!(loginMember instanceof Seller)){
            return "redirect:/";
        }
        ItemUpdateDto itemUpdateDto = itemService.getItemDto(id);
        model.addAttribute("itemUpdateDto", itemUpdateDto);

        Map<String, String> itemTypes = Arrays.stream(ItemType.values())
                .collect(Collectors.toMap(Enum::name, ItemType::getKoreanName));

        model.addAttribute("itemTypes", itemTypes);
        return "item/edit";
    }

    @PostMapping("/item/edit")
    public String updateItem(@ModelAttribute(name = "itemUpdateDto") ItemUpdateDto itemUpdateDto,
                             RedirectAttributes redirectAttributes,
                             HttpSession session){
        Object loginMember = session.getAttribute("loginMember");
        if (loginMember instanceof Seller seller){
            itemUpdateDto.setSellerId(seller.getId());

            if (itemUpdateDto.getThumbnailImage() != null && !itemUpdateDto.getThumbnailImage().isEmpty()){
                String thumbnailImageUrl = saveImage(itemUpdateDto.getThumbnailImage());
                itemUpdateDto.setThumbnailImageUrl(thumbnailImageUrl);
            }

            List<String> detailImageUrls = new ArrayList<>();
            for (MultipartFile detailImage : itemUpdateDto.getDetailImages()){
                if (!detailImage.isEmpty()){
                    String detailImageUrl = saveImage(detailImage);
                    detailImageUrls.add(detailImageUrl);
                }
            }
            itemUpdateDto.setDetailImageUrls(detailImageUrls);

            itemService.updateItem(itemUpdateDto);
            redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 수정되었습니다.");
            return "redirect:/item/register-list";
        }
        return "redirect:/";
    }

    @PostMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable(name = "id") Long id,
                             RedirectAttributes redirectAttributes){
        itemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/item/register-list";
    }

    @GetMapping("/item/list")
    public String showItemList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "sortType", required = false) String sortType,
            Model model){
        Pageable pageable = PageRequest.of(page, size);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setSortType(sortType);
        Page<ItemListDto> pagedItems = itemService.searchItems(itemSearchDto, pageable);
        model.addAttribute("items", pagedItems.getContent());
        model.addAttribute("page", pagedItems);
        model.addAttribute("sortType", sortType);
        return "item/list";
    }

    @GetMapping("/items")
    public String searchItems(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "itemType", required = false) ItemType itemType,
                              @RequestParam(value = "sortType", required = false) String sortType,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model){
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setKeyword(keyword);
        itemSearchDto.setItemType(itemType);
        itemSearchDto.setSortType(sortType);
        Pageable pageable = PageRequest.of(page, size);

        Page<ItemListDto> pagedSearchItems = itemService.searchItems(itemSearchDto, pageable);
        model.addAttribute("items", pagedSearchItems.getContent());
        model.addAttribute("page", pagedSearchItems);
        model.addAttribute("sortType", sortType);

        return "item/list";
    }

    @GetMapping("/item/detail/{itemId}")
    public String getItemDetail(@PathVariable(name = "itemId") Long itemId,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                Model model,
                                HttpSession session){

        Customer loginMember = (Customer) session.getAttribute("loginMember");

        if (loginMember != null){
            model.addAttribute("loginMember", loginMember);
        }

        Optional<ItemDetailDto> itemDetail = itemService.getItemDetail(itemId, PageRequest.of(page, size));

        if (itemDetail.isPresent()){
            model.addAttribute("itemDetail", itemDetail.get());
            Page<ReviewDto> pagedReviews = reviewService.getPagedReviews(itemId, PageRequest.of(page, size));
            model.addAttribute("pagedReviews", pagedReviews);
        } else {
            return "redirect:/";
        }

        return "item/detail";
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
