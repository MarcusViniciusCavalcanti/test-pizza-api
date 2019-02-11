package com.test.api.pizza.application.endpoint;

import com.test.api.pizza.application.dto.AddonsDTO;
import com.test.api.pizza.application.service.AddonsService;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.structure.execptions.InvalidParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/addons")
public class AddonsEndpoint {

    private final AddonsService addonsService;

    @Autowired
    public AddonsEndpoint(AddonsService addonsService) {
        this.addonsService = addonsService;
    }

    @GetMapping
    public ResponseEntity<List<? extends Addons>> getAll() {
        return new ResponseEntity<>(addonsService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends Addons> getById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(addonsService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Addons> create(@Valid @RequestBody AddonsDTO addonsDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        return new ResponseEntity<>(addonsService.save(addonsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Addons> update(@PathVariable("id") Integer id, @Valid @RequestBody AddonsDTO addonsDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        return new ResponseEntity<>(addonsService.update(id, addonsDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        addonsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
