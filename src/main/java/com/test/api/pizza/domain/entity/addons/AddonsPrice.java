package com.test.api.pizza.domain.entity.addons;

import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.Flavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "addons_price")
@Data
@EqualsAndHashCode(callSuper = true)
public class AddonsPrice extends Addons {
    @Column
    private Double price;

    @Override
    public void calculate(Order order) {
        Double totalPrice = order.getTotalPrice() + price;
        order.setTotalPrice(totalPrice);
    }

    @Override
    public Object value() {
        return this.price;
    }
}
