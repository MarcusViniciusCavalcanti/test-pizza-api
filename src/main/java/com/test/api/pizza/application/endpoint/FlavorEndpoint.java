package com.test.api.pizza.application.endpoint;

import com.test.api.pizza.application.dto.FlavorDTO;
import com.test.api.pizza.application.service.FlavorService;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.structure.execptions.InvalidParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/flavors")
public class FlavorEndpoint {

    private final FlavorService flavorService;

    @Autowired
    public FlavorEndpoint(FlavorService flavorService) {
        this.flavorService = flavorService;
    }

    @GetMapping
    public ResponseEntity<List<Flavor>> getAll() {
        return new ResponseEntity<>(flavorService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flavor> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(flavorService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Flavor> create(@Valid @RequestBody FlavorDTO flavorDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        return new ResponseEntity<>(flavorService.save(flavorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flavor> update(@PathVariable("id") Integer id, @Valid @RequestBody FlavorDTO flavorDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        return new ResponseEntity<>(flavorService.update(id, flavorDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        flavorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

