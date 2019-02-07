package com.test.api.product.application.service;

import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import com.test.api.product.domain.resouce.ProductTypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public List<ProductTypeResource> getAll() {
        return null;
    }

    public ProductTypeResource getById(Integer id) {
        return null;
    }

    public ProductTypeResource save(ProductTypeDTO productTypeDTO) {
        return null;
    }

    public ProductTypeResource update(Integer id, ProductTypeDTO productTypeDTO) {
        return null;
    }
}
