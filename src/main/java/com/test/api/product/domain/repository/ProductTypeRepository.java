package com.test.api.product.domain.repository;

import com.test.api.product.domain.entity.ProductType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends CrudRepository<ProductType, Integer> {
}
