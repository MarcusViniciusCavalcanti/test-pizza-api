package com.test.api.pizza.application.endpoint;

import com.test.api.pizza.domain.valueObject.size.SizePizza;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/sizes-pizza")
public class SizeEndpoint {

    @GetMapping
    public ResponseEntity<List<String>> getAllSize() {
        var sizes = Arrays.stream(SizePizza.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sizes, HttpStatus.OK);
    }
}
