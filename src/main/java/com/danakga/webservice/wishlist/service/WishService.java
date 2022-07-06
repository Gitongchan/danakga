package com.danakga.webservice.wishlist.service;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.user.model.UserInfo;
import com.danakga.webservice.user.model.UserRole;
import com.danakga.webservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface WishService {
    //좋아요 기능
    Long wishList(UserInfo userInfo, Long product_id);
    Long wishCheck(UserInfo userInfo, Long product_id);
    Long wishDelete(UserInfo userInfo,Long product_id);
}
