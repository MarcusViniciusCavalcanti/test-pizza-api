package com.test.api.pizza.application.endpoint;

import com.test.api.pizza.application.service.CustomizationsService;
import com.test.api.pizza.domain.entity.Customization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/customizations")
public class CustomizationsEndpoint {

    private final CustomizationsService customizationsService;

    @Autowired
    public CustomizationsEndpoint(CustomizationsService customizationsService) {
        this.customizationsService = customizationsService;
    }

    @GetMapping
    public ResponseEntity<List<Customization>> getAll() {
        return new ResponseEntity<>(customizationsService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customization> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(customizationsService.getById(id), HttpStatus.OK);
    }
}
