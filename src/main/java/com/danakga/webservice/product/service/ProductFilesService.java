package com.danakga.webservice.product.service;

import com.danakga.webservice.product.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductFilesService {
    Long uploadFile(List<MultipartFile> files, Product product);

    String thumbFile(MultipartFile thumb);
}
