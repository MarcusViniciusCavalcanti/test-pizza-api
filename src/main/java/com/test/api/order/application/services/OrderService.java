package com.test.api.order.application.services;

import com.test.api.order.application.dtos.ResumeOrderDTO;
import com.test.api.order.application.dtos.OrderDTO;
import com.test.api.order.domain.entity.Order;
import com.test.api.order.domain.repository.OrderRepository;
import com.test.api.pizza.application.service.CustomizationsService;
import com.test.api.pizza.application.service.FlavorService;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final FlavorService flavorService;
    private final CustomizationsService customizationsService;

    @Autowired
    public OrderService(OrderRepository orderRepository, FlavorService flavorService, CustomizationsService customizationsService) {
        this.orderRepository = orderRepository;
        this.flavorService = flavorService;
        this.customizationsService = customizationsService;
    }

    public ResumeOrderDTO closeOrder(OrderDTO orderDTO) {
        var order = new Order();

        var size = SizePizza.valueOf(orderDTO.getPizzaSize().toUpperCase());
        var flavor = flavorService.getById(orderDTO.getFlavorId());

        var resumeOrder = new ResumeOrderDTO();
        var detailsSize = new ResumeOrderDTO.OrderCloseDetailsSize();

        var listCustomizations = orderDTO.getCustomizations();
        if (listCustomizations != null && !listCustomizations.isEmpty()) {
            var customizations = customizationsService.getByIds(listCustomizations);
            order.setCustomizations(customizations);

            var details = new ArrayList<ResumeOrderDTO.OrderCloseDetailsCustomizations>();
            customizations.forEach(customization -> {
                var detail = new ResumeOrderDTO.OrderCloseDetailsCustomizations();
                customization.getAddons().stream()
                        .filter(addons -> addons.value() instanceof Double)
                        .forEach(addons -> {
                            detail.setName(customization.getName());
                            detail.setValue((Double) addons.value());
                            details.add(detail);
                        });
            });

            resumeOrder.setCustomizationsDetails(details);
        }

        order.setSizePizza(size);
        order.setFlavor(flavor);

        order.closeOrder();

        orderRepository.save(order);

        detailsSize.setSize(order.getSizePizza().name());
        detailsSize.setValue(order.getSizePizza().getSize().value());

        resumeOrder.setOrderId(order.getId());
        resumeOrder.setSizeDetails(detailsSize);
        resumeOrder.setTimeProcess(order.getTimeProcess());
        resumeOrder.setTotal(order.getTotalPrice());

        return resumeOrder;
    }

    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order by id: " + id + " Not found"));
    }
}
