package com.test.api.pizza.domain.entity.addons;

import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.Flavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Table(name = "addons_time_process")
@Entity
@Data
public class AddonsTimeProcess extends Addons {

    @Column
    private Integer time;

    @Override
    public void calculate(Order order) {
        Integer timeProcess = order.getTimeProcess();
        order.setTimeProcess(timeProcess + time);
    }

    @Override
    public Object value() {
        return this.time;
    }
}
