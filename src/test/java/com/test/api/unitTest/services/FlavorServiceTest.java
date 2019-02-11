package com.test.api.unitTest.services;

import com.test.api.pizza.application.dto.FlavorDTO;
import com.test.api.pizza.application.service.AddonsService;
import com.test.api.pizza.application.service.FlavorService;
import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
import com.test.api.pizza.domain.repository.FlavorRepository;
import com.test.api.utils.FactoryObjectsToTest;
import com.test.api.utils.factories.FlavorFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Optional;

import static com.test.api.utils.factories.FlavorFactory.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class FlavorServiceTest {

    @Configuration
    static class config {
        @Autowired
        FlavorRepository flavorRepository;

        @Autowired
        AddonsService addonsService;

        @Bean
        public FlavorService getProductTypeService() {
            return new FlavorService(flavorRepository, addonsService);
        }
    }

    @Autowired
    private FlavorService flavorService;

    @MockBean
    private FlavorRepository flavorRepository;

    @MockBean
    private AddonsService addonsService;

    private FactoryObjectsToTest<Flavor> flavorFactory = new FlavorFactory();

    @Test(expected = EntityNotFoundException.class)
    public void should_have_throw_exception_when_id_not_exist() {
        flavorService.getById(ID_NOT_FOUND);
    }

    @Test
    public void should_return_successfully_by_id() {
        var calabresa = flavorFactory.createOneObject();
        Mockito.when(flavorRepository.findById(ID_CALABRESA)).thenReturn(Optional.of(calabresa));

        var result = flavorService.getById(ID_CALABRESA);
        assertThat(result.getName(), is(calabresa.getName()));
        assertThat(result.getAddons(), nullValue());
    }

    @Test
    public void should_return_all_flavors() {
        var list = flavorFactory.createListObjects();
        Mockito.when(flavorRepository.findAll()).thenReturn(list);

        var result = flavorService.getAll();
        assertThat(result.size(), is(list.size()));
        assertThat(result, containsInAnyOrder(list.toArray()));
    }

    @Test
    public void should_save_successfully_not_addons() {
        var args = new HashMap<String, Object>();
        args.put("id", 1);
        args.put("name", "nome");
        args.put("addons", null);

        var flavor = flavorFactory.createObjectBy(args);

        var dto = new FlavorDTO();
        dto.setName("nome");

        Mockito.when(flavorRepository.save(Mockito.any(Flavor.class))).thenReturn(flavor);

        var result = flavorService.save(dto);

        verify(flavorRepository, times(1)).save(Mockito.any(Flavor.class));

        assertThat(result.getName(), CoreMatchers.is(flavor.getName()));
        assertThat(result.getId(), CoreMatchers.notNullValue());
    }

    @Test
    public void should_save_successfully_with_addons() {
        var addonTimeProcess = new AddonsTimeProcess();
        addonTimeProcess.setId(1);

        var args = new HashMap<String, Object>();
        args.put("id", 1);
        args.put("name", "nome bacana");
        args.put("addons", addonTimeProcess);

        var flavor = flavorFactory.createObjectBy(args);
        flavor.setAddons(addonTimeProcess);

        var dto = new FlavorDTO();
        dto.setName("nome bacana");
        dto.setAddonsId(1);

        Mockito.when(addonsService.getById(1)).thenReturn(addonTimeProcess);
        Mockito.when(flavorRepository.save(Mockito.any(Flavor.class))).thenReturn(flavor);

        var result = flavorService.save(dto);

        verify(flavorRepository, times(1)).save(Mockito.any(Flavor.class));
        verify(addonsService, times(1)).getById(1);

        assertThat(result.getName(), CoreMatchers.is(flavor.getName()));
        assertThat(result.getId(), CoreMatchers.notNullValue());
        assertThat(result.getAddons(), CoreMatchers.notNullValue());
    }
}