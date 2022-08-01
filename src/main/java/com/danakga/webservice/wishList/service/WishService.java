package com.danakga.webservice.wishList.service;

import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.wishList.dto.request.WishIdDto;
import com.danakga.webservice.wishList.dto.response.ResWishListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface WishService {
    //좋아요 기능
    Long wishProcess(UserInfo userInfo, Long product_id);

    List<ResWishListDto> wishList(UserInfo userInfo, Pageable pageable, int page);

    Long wishDelete(UserInfo userInfo , List<WishIdDto> wishIdDto);

    Long wishCheck(UserInfo userInfo,Long productId);
}
