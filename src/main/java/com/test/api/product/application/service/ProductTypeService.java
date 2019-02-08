package com.test.api.product.application.service;

import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.domain.assemble.ProductTypeAssemble;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.repository.ProductTypeRepository;
import com.test.api.product.domain.resource.ProductTypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductTypeResource> getAll() {
        var productsTypes = productTypeRepository.findAll();
        var assemble = new ProductTypeAssemble();
        return assemble.toResources(productsTypes);
    }

    @Transactional(readOnly = true)
    public ProductTypeResource getById(Integer id) {
        var productType = getProductType(id);
        var assemble = new ProductTypeAssemble();
        return assemble.toResource(productType);
    }

    @Transactional
    public ProductTypeResource save(ProductTypeDTO productTypeDTO) {
        var productType = new ProductType();
        replaceEntity(productTypeDTO, productType);
        var assemble = new ProductTypeAssemble();
        return assemble.toResource(productTypeRepository.save(productType));
    }

    @Transactional
    public ProductTypeResource update(Integer id, ProductTypeDTO productTypeDTO) {
        var productType = getProductType(id);
        replaceEntity(productTypeDTO, productType);
        var assemble = new ProductTypeAssemble();
        return assemble.toResource(productType);
    }

    private void replaceEntity(ProductTypeDTO dto, ProductType productType) {
        productType.setName(dto.getName());
        productType.setTimeProcess(dto.getTimeProcess());
    }

    private ProductType getProductType(Integer id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductType by id: " + id + " Not found"));
    }
}
