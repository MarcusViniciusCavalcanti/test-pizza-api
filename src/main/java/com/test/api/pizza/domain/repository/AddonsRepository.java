package com.test.api.pizza.domain.repository;

import com.test.api.pizza.domain.entity.addons.Addons;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddonsRepository extends CrudRepository<Addons, Integer> {

    List<Addons> findAll();
}
