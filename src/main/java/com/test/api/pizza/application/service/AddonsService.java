package com.test.api.pizza.application.service;

import com.test.api.pizza.application.dto.AddonsDTO;
import com.test.api.pizza.domain.handlers.TypeAddendHandler;
import com.test.api.pizza.domain.repository.AddonsRepository;
import com.test.api.pizza.domain.entity.addons.Addons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
public class AddonsService {

    private final AddonsRepository addonsRepository;

    @Autowired
    public AddonsService(AddonsRepository addonsRepository) {
        this.addonsRepository = addonsRepository;
    }

    @Transactional
    public List<Addons> getAll() {
        return addonsRepository.findAll();
    }

    @Transactional
    public Addons getById(Integer id) {
        return addonsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Addons by id: " + id + " Not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public Addons save(AddonsDTO addonsDTO) {
        var addons = createNewAddons(addonsDTO);
        return addonsRepository.save(addons);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public Addons update(Integer id, AddonsDTO addonsDTO) {
        var editAddons = getById(id);
        Addons newAddonsValues = createNewAddons(addonsDTO);
        newAddonsValues.setId(editAddons.getId());
        newAddonsValues.setName(addonsDTO.getName());
        return addonsRepository.save(newAddonsValues);
    }

    private Addons createNewAddons(AddonsDTO addonsDTO) {
        var addons = new TypeAddendHandler().getAddendsByValueType(addonsDTO.getValue());
        addons.setName(addonsDTO.getName());

        return addons;
    }

    public void delete(Integer id) {
        addonsRepository.deleteById(id);
    }
}
