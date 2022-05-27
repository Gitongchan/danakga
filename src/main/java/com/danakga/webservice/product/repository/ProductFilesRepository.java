package com.danakga.webservice.product.repository;

import com.danakga.webservice.product.model.ProductFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFilesRepository extends JpaRepository<ProductFiles,Integer> {
}
