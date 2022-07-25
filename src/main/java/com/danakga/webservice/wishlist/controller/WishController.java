package com.danakga.webservice.wishlist.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.dto.response.ResDupliCheckDto;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.repository.UserRepository;
import com.danakga.webservice.util.responseDto.ResResultDto;
import com.danakga.webservice.wishlist.dto.request.WishProductDto;
import com.danakga.webservice.wishlist.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class WishController {
// 파라미터 받아서 작성 -> 레파지 토리 작성 -> 서비스 작성

    private final WishService wishService;

    // 찜 등록 , 삭제
    @PostMapping(value = "/wish")
    public ResResultDto wishListLike(@LoginUser UserInfo userInfo, @RequestBody WishProductDto wishProductDto){
        Long result = wishService.wishProcess(userInfo,wishProductDto.getProductId());
        return result == -1L ?
        new ResResultDto(result,"위시리스트에서 제거했습니다.") : new ResResultDto(result,"위시리스트에 등록했습니다.");
    }

}
