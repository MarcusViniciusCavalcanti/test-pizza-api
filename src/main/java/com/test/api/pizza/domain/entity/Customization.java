package com.test.api.pizza.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.addons.Addons;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "customizations")
public class Customization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customizations_addons",
            joinColumns = {@JoinColumn(name = "customizations_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "addons_id", referencedColumnName = "id")}
    )
    private Set<Addons> addons;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customizations_orders",
            joinColumns = {@JoinColumn(name = "customizations_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "orders_id", referencedColumnName = "id")}
    )
    private List<Order> orders;

    @JsonIgnore
    @Column
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column
    private LocalDateTime updatedAt;

    public void addAddons(Addons ...addonsThreePrice) {
        this.addons = new HashSet<>();
        this.addons.addAll(Arrays.asList(addonsThreePrice));
    }

    @PrePersist
    private void setDateCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setDateUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}
