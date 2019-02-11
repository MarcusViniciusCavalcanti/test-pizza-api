package com.test.api;

import com.test.api.order.domain.entity.Order;
import com.test.api.order.domain.repository.OrderRepository;
import com.test.api.pizza.domain.entity.Customization;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.AddonsPrice;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
import com.test.api.pizza.domain.repository.AddonsRepository;
import com.test.api.pizza.domain.repository.CustomizationRepository;
import com.test.api.pizza.domain.repository.FlavorRepository;
import com.test.api.pizza.domain.valueObject.size.SizePizza;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@SpringBootApplication
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
public class ApiApplication implements CommandLineRunner {

    @Autowired
    private AddonsRepository addonsRepository;

    @Autowired
    private FlavorRepository flavorRepository;

    @Autowired
    private CustomizationRepository customizationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(environment.getActiveProfiles()).contains("development")) {
            initializeDatabase();
        }
    }

    private void initializeDatabase() {
        var timeProcess = new AddonsTimeProcess();
        timeProcess.setTime(5);
        timeProcess.setName("add five minute in process");

        var addFivePrice = new AddonsPrice();
        addFivePrice.setPrice(5.0);
        addFivePrice.setName("add five in price");

        var addThreePrice = new AddonsPrice();
        addThreePrice.setPrice(3.0);
        addThreePrice.setName("add three in price");

        AddonsTimeProcess addonsTimeProcess = addonsRepository.save(timeProcess);
        AddonsPrice addonsFivePrice = addonsRepository.save(addFivePrice);
        AddonsPrice addonsThreePrice = addonsRepository.save(addThreePrice);


        var calabresa = new Flavor();
        calabresa.setName("Calabresa");

        var marguerita = new Flavor();
        marguerita.setName("marguerita");

        var portuguesa = new Flavor();
        portuguesa.setName("portuguesa");
        portuguesa.setAddons(addonsTimeProcess);

        flavorRepository.saveAll(Arrays.asList(calabresa, marguerita, portuguesa));

        var bacon = new Customization();
        bacon.setName("Extra Bacon");
        bacon.addAddons(addonsThreePrice);

        var withoutCebola = new Customization();
        withoutCebola.setName("Sem cebola");

        var border = new Customization();
        border.setName("Bordar Recheada");
        border.addAddons(addonsFivePrice, addonsTimeProcess);

        Customization saveBacon = customizationRepository.save(bacon);
        Customization saveWithoutCebola = customizationRepository.save(withoutCebola);
        Customization saveBorder = customizationRepository.save(border);
    }
}

