package com.test.api.pizza.application.service;

import com.test.api.pizza.application.dto.FlavorDTO;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.repository.FlavorRepository;
import com.test.api.pizza.domain.entity.addons.Addons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class FlavorService {

    private final FlavorRepository flavorRepository;
    private final AddonsService addonsService;

    @Autowired
    public FlavorService(FlavorRepository flavorRepository, AddonsService addonsService) {
        this.flavorRepository = flavorRepository;
        this.addonsService = addonsService;
    }

    @Transactional
    public Flavor getById(Integer id) {
        return flavorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Flavor by id: " + id + " Not found"));
    }

    @Transactional
    public List<Flavor> getAll() {
        return flavorRepository.findAll();
    }

    @Transactional
    public Flavor save(FlavorDTO dto) {
        var flavor =  replaceAttributesByDTO(new Flavor(), dto);
        return flavorRepository.save(flavor);
    }

    public Flavor update(Integer id, FlavorDTO flavorDTO) {
        var flavor = getById(id);
        flavor = replaceAttributesByDTO(flavor, flavorDTO);
        return flavorRepository.save(flavor);
    }

    private Flavor replaceAttributesByDTO(Flavor flavor, FlavorDTO dto) {
        flavor.setName(dto.getName());

        if (dto.getAddonsId() != null) {
            Addons addons = addonsService.getById(dto.getAddonsId());
            flavor.setAddons(addons);
        }

        return  flavor;
    }

    public void delete(Integer id) {
        flavorRepository.deleteById(id);
    }
}
