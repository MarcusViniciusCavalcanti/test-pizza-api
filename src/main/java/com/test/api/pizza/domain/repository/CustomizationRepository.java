package com.test.api.pizza.domain.repository;

import com.test.api.pizza.domain.entity.Customization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CustomizationRepository extends CrudRepository<Customization, Integer> {

    List<Customization> findAll();

    default List<Customization> findAllByIds(List<Integer> ids) {
        List<Customization> list = new ArrayList<>();
        this.findAllById(ids).forEach(list::add);
        return list;
    }
}
