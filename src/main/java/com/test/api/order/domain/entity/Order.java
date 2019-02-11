package com.test.api.order.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.api.pizza.domain.entity.Customization;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.pizza.domain.valueObject.size.Size;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private Integer timeProcess;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customizations_orders",
            joinColumns = {@JoinColumn(name = "orders_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "customizations_id", referencedColumnName = "id")}
    )
    private List<Customization> customizations;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flavors_id")
    private Flavor flavor;

    @Enumerated(value = EnumType.STRING)
    private SizePizza sizePizza;

    public void closeOrder() {
        totalPrice = sizePizza.getSize().value();
        timeProcess = sizePizza.getSize().timeProcess();

        if (customizations != null) {
            customizations.stream()
                    .filter(customization -> customization.getAddons() != null)
                    .flatMap(customization -> customization.getAddons()
                    .stream())
                    .filter(Objects::nonNull)
                    .forEach(addons -> addons.calculate(this));

        }

        if (flavor != null && flavor.getAddons() != null) {
            flavor.getAddons().calculate(this);
        }
    }
}
