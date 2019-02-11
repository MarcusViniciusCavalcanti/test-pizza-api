package com.test.api.pizza.domain.entity.addons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.test.api.order.application.services.OrderService;
import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.Customization;
import com.test.api.pizza.domain.entity.Flavor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Addons {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(nullable = false, unique = true, length = 50)
    protected String name;

    @JsonBackReference
    @OneToMany(
            mappedBy = "addons",
            cascade = CascadeType.ALL
    )
    private List<Flavor> flavors;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customizations_addons",
            joinColumns = {@JoinColumn(name = "addons_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "customizations_id", referencedColumnName = "id")}
    )
    private List<Customization> customizations;

    public abstract void calculate(Order order);

    public abstract Object value();
}
