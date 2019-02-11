package com.test.api.pizza.domain.repository;

import com.test.api.pizza.domain.entity.Flavor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FlavorRepository extends PagingAndSortingRepository<Flavor, Integer> {
    List<Flavor> findAll();
}
