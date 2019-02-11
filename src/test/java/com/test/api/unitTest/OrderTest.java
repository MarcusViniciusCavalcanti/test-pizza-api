package com.test.api.unitTest;

import com.test.api.order.domain.entity.Order;
import com.test.api.pizza.domain.entity.Customization;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.AddonsPrice;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;

public class OrderTest {

    @Test
    public void should_have_correct_close_oder_when_small() {
        var order = new Order();
        order.setSizePizza(SizePizza.SMALL);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(15));
        assertThat(order.getTotalPrice(), CoreMatchers.is(20.0));
    }

    @Test
    public void should_have_correct_close_oder_when_middle() {
        var order = new Order();
        order.setSizePizza(SizePizza.MIDDLE);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(20));
        assertThat(order.getTotalPrice(), CoreMatchers.is(30.0));
    }

    @Test
    public void should_have_correct_close_oder_when_bigger() {
        var order = new Order();
        order.setSizePizza(SizePizza.BIGGER);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(25));
        assertThat(order.getTotalPrice(), CoreMatchers.is(40.0));
    }


    @Test
    public void should_have_correct_close_order_when_select_flavor_without_behavior() {
        var order = new Order();
        order.setSizePizza(SizePizza.BIGGER);
        var flavor = new Flavor();
        order.setFlavor(flavor);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(25));
        assertThat(order.getTotalPrice(), CoreMatchers.is(40.0));
    }

    @Test
    public void should_have_correct_close_order_when_select_flavor_with_behavior() {
        var order = new Order();
        var flavor = new Flavor();
        var addFiveTime = new AddonsTimeProcess();

        addFiveTime.setTime(5);
        flavor.setAddons(addFiveTime);

        order.setSizePizza(SizePizza.SMALL);
        order.setFlavor(flavor);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(20));
        assertThat(order.getTotalPrice(), CoreMatchers.is(20.0));
    }

    @Test
    public void should_have_calculate_time_when_add_extras() {
        var order = new Order();
        var flavor = new Flavor();
        var addFiveTime = new AddonsTimeProcess();

        addFiveTime.setTime(5);

        var border = new Customization();

        border.addAddons(addFiveTime);
        var customizations = new ArrayList<Customization>();
        customizations.add(border);

        order.setSizePizza(SizePizza.SMALL);
        order.setFlavor(flavor);
        order.setCustomizations(customizations);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(20));
    }

    @Test
    public void should_have_calculate_price_when_add_extras() {
        var order = new Order();
        var flavor = new Flavor();
        var addThreePrice = new AddonsPrice();

        addThreePrice.setPrice(3.0);

        var bacon = new Customization();

        bacon.addAddons(addThreePrice);
        var customizations = new ArrayList<Customization>();
        customizations.add(bacon);

        order.setSizePizza(SizePizza.SMALL);
        order.setFlavor(flavor);
        order.setCustomizations(customizations);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(15));
        assertThat(order.getTotalPrice(), CoreMatchers.is(23.0));
    }


    @Test
    public void should_have_calculate_price_and_time_when_add_extras() {
        var order = new Order();
        var flavor = new Flavor();
        var addFiveTime = new AddonsTimeProcess();
        var addFivePrice = new AddonsPrice();

        addFiveTime.setTime(5);
        addFivePrice.setPrice(5.0);

        var border = new Customization();

        border.addAddons(addFiveTime, addFivePrice);
        var customizations = new ArrayList<Customization>();
        customizations.add(border);

        order.setSizePizza(SizePizza.BIGGER);
        order.setFlavor(flavor);
        order.setCustomizations(customizations);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(30));
        assertThat(order.getTotalPrice(), CoreMatchers.is(45.0));
    }

    @Test
    public void should_have_close_order_without_extras() {
        var order = new Order();
        var flavor = new Flavor();
        var addFiveTime = new AddonsTimeProcess();

        addFiveTime.setTime(5);
        flavor.setAddons(addFiveTime);

        order.setSizePizza(SizePizza.BIGGER);
        order.setFlavor(flavor);

        order.closeOrder();

        assertThat(order.getTimeProcess(), CoreMatchers.is(30));
        assertThat(order.getTotalPrice(), CoreMatchers.is(40.0));
    }
}