package com.test.api.pizza.application.service;

import com.test.api.pizza.domain.entity.Customization;
import com.test.api.pizza.domain.repository.CustomizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CustomizationsService {

    private final CustomizationRepository customizationRepository;

    @Autowired
    public CustomizationsService(CustomizationRepository customizationRepository) {
        this.customizationRepository = customizationRepository;
    }

    @Transactional
    public List<Customization> getAll() {
        return customizationRepository.findAll();
    }

    public Customization getById(Integer id) {
        return customizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customization by id: " + id + " Not found"));
    }

    @Transactional
    public List<Customization> getByIds(List<Integer> customizations) {
        return customizationRepository.findAllByIds(customizations);
    }
}
