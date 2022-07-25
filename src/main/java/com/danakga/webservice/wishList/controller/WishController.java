package com.danakga.webservice.wishList.controller;

import com.danakga.webservice.annotation.LoginUser;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.util.responseDto.ResResultDto;
import com.danakga.webservice.wishList.dto.request.WishProductDto;
import com.danakga.webservice.wishList.dto.response.ResWishListDto;
import com.danakga.webservice.wishList.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class WishController {
// 파라미터 받아서 작성 -> 레파지 토리 작성 -> 서비스 작성

    private final WishService wishService;

    // 위시 리스트 등록 , 삭제
    @PostMapping(value = "/wish")
    public ResResultDto wishProcess(@LoginUser UserInfo userInfo, @RequestBody WishProductDto wishProductDto){
        Long result = wishService.wishProcess(userInfo,wishProductDto.getProductId());
        return result == -1L ?
        new ResResultDto(result,"위시리스트에서 제거했습니다.") : new ResResultDto(result,"위시리스트에 등록했습니다.");
    }
    
    //로그인 사용자의 위시 리스트
    @GetMapping(value = "/wish/{page}")
    public List<ResWishListDto> wishList(@LoginUser UserInfo userInfo, Pageable pageable,@PathVariable int page){
        return wishService.wishList(userInfo,pageable,page);
    }


}
