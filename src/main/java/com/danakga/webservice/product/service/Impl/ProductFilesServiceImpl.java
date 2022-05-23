package com.danakga.webservice.product.service.Impl;

import com.danakga.webservice.board.model.Board_Files;
import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.model.ProductFiles;
import com.danakga.webservice.product.repository.ProductFilesRepository;
import com.danakga.webservice.product.repository.ProductRepository;
import com.danakga.webservice.product.service.ProductFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductFilesServiceImpl implements ProductFilesService {

    private final ProductFilesRepository productFilesRepository;

    @Override
    @Transactional
    public Long uploadFile(List<MultipartFile> files, Product product){

        Long pf_id = null;

        //파일 저장 경로
        String savepath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\product_files\\";

        //파일 저장되는 폳더 없으면 생성
        if(!new File(savepath).exists()) {
            try{
                new File(savepath).mkdir();
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        // 다중 파일 처리
        // multipartfile : files  files에서 더 꺼낼게 없을 때 까지 multipartfile에 담아줌
        for(MultipartFile multipartFile : files) {

            //파일명 소문자로 추출
            String originFileName = multipartFile.getOriginalFilename().toLowerCase();

            //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
            UUID uuid = UUID.randomUUID();
            String saveFileName = uuid + "__" + originFileName;

            //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
            String filepath = savepath + "\\" + saveFileName;
            System.out.println(filepath);
            try {
                multipartFile.transferTo(new File(filepath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            pf_id = productFilesRepository.save(
                    ProductFiles.builder()
                            .product(product)
                            .pf_origin(originFileName)
                            .pf_path(filepath)
                            .pf_savename(saveFileName)
                            .build()
            ).getPf_id();
        }
        return pf_id;

    }
}
