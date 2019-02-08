package com.test.api.product.application.endpoint;

import com.test.api.product.application.dto.ProductTypeDTO;
import com.test.api.product.application.service.ProductTypeService;
import com.test.api.product.domain.resource.ProductTypeResource;
import com.test.api.structure.execptions.InvalidParamsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/product-types")
public class ProductTypeEndpoint {

    Logger logger = LoggerFactory.getLogger(ProductTypeEndpoint.class);

    private final ProductTypeService productTypeService;

    @Autowired
    public ProductTypeEndpoint(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResource>> getAll() {
        var resources = productTypeService.getAll();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResource> getById(@PathVariable("id") Integer id) {
        var resource = productTypeService.getById(id);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductTypeResource> create(@Valid @RequestBody ProductTypeDTO productTypeDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }
        var resource = productTypeService.save(productTypeDTO);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResource> update(@PathVariable("id") Integer id,
                                                      @Valid @RequestBody ProductTypeDTO productTypeDTO, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        var resource = productTypeService.update(id, productTypeDTO);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}
