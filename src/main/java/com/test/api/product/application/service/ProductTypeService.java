package com.test.api.product.application.service;

import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.domain.assemble.ProductTypeAssemble;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import com.test.api.product.domain.resouce.ProductTypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public List<ProductTypeResource> getAll() {
        var productsTypes = productTypeRepository.findAll();
        var assemble = new ProductTypeAssemble();
        return assemble.toResources(productsTypes);
    }

    public ProductTypeResource getById(Integer id) {
        var productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductType by id: " + id + " Not found"));
        var assemble = new ProductTypeAssemble();
        return assemble.toResource(productType);
    }

    public ProductTypeResource save(ProductTypeDTO productTypeDTO) {
        return null;
    }

    public ProductTypeResource update(Integer id, ProductTypeDTO productTypeDTO) {
        return null;
    }
}
