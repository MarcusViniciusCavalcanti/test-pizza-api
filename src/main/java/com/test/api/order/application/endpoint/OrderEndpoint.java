package com.test.api.order.application.endpoint;

import com.test.api.order.application.dtos.OrderDTO;
import com.test.api.order.application.dtos.ResumeOrderDTO;
import com.test.api.order.application.services.OrderService;
import com.test.api.order.domain.entity.Order;
import com.test.api.structure.execptions.InvalidParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/orders")
public class OrderEndpoint {

    private final OrderService orderService;

    @Autowired
    public OrderEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getAll(Pageable pageable) {
        return new ResponseEntity<>(orderService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/close")
    public ResponseEntity<ResumeOrderDTO> newOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParamsException("params invalid", result);
        }

        var order = orderService.closeOrder(orderDTO);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}
