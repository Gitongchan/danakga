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
public class WishController {
// 파라미터 받아서 작성 -> 레파지 토리 작성 -> 서비스 작성

    private final WishService wishService;
    // 유저 리파지토르 사용 > save -> 사용

    // 찜목록 좋아요
    @PostMapping(value = "/wishlist")
    public ResResultDto wishListLike(@LoginUser UserInfo userInfo, @RequestBody WishProductDto wishProductDto){
        Long result = wishService.wishList(userInfo,wishProductDto.getPd_id());
        return new ResResultDto(result,"찜 등록 완료했습니다.");
    }

    //찜 체크
    @GetMapping("/wishlist_check")
    public ResDupliCheckDto wishListCheck(@LoginUser UserInfo userInfo, @RequestBody WishProductDto wishProductDto){
        return new ResDupliCheckDto(wishService.wishCheck(userInfo,wishProductDto.getPd_id()));
    }
    // 찜 삭제
    @PostMapping
    public ResResultDto wishDelete(@LoginUser UserInfo userInfo, @RequestBody WishProductDto wishProductDto) {
        return null;
    }
}
