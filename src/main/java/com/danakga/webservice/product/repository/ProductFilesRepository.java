package com.danakga.webservice.product.repository;

import com.danakga.webservice.product.model.Product;
import com.danakga.webservice.product.model.ProductFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFilesRepository extends JpaRepository<ProductFiles,Integer> {
    List<ProductFiles> findByProduct(Product product);

    void deleteByProductAndPfSaveName(Product product,String PfSaveName);

    void deleteByProductAndPfPath(Product product,String pfPath);

    void deleteAllByProduct(Product product);




}
