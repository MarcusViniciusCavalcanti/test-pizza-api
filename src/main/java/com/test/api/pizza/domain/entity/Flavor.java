package com.test.api.pizza.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.addons.Addons;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flavors")
@Data
public class Flavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addon_id")
    private Addons addons;

    @JsonBackReference
    @OneToMany(
            mappedBy = "flavor",
            cascade = CascadeType.PERSIST
    )
    private List<Order> order;

    @JsonIgnore
    @Column
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column
    private LocalDateTime updatedAt;

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
