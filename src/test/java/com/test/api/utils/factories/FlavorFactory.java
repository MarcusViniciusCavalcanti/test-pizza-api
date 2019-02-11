package com.test.api.utils.factories;

import com.test.api.pizza.domain.entity.Flavor;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;
import com.test.api.utils.FactoryObjectsToTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlavorFactory extends FactoryObjectsToTest<Flavor> {
    public static Integer ID_CALABRESA = 1;
    public static Integer ID_MARGERITA = 2;
    public static Integer ID_PORTUGUESA = 3;
    public static Integer ID_NOT_FOUND = 0;

    public static String NAME_CALABRESA = "CALABRESA";
    public static String NAME_MARGUERITA = "MARGUERITA";
    public static String NAME_PORTUGUESA = "PORTUGUESA";

    @Override
    protected Flavor createSample() {
        return create(ID_CALABRESA, NAME_CALABRESA, null);
    }

    @Override
    protected List<Flavor> createList() {
        var addon = new AddonsTimeProcess();

        var calabresa = create(ID_CALABRESA, NAME_CALABRESA, null);
        var marguerita = create(ID_MARGERITA, NAME_MARGUERITA, null);
        var portuguresa = create(ID_PORTUGUESA, NAME_PORTUGUESA, addon);
        return Arrays.asList(calabresa, marguerita, portuguresa);
    }

    @Override
    protected Flavor createBy(Map<String, Object> args) {
        var name = (String) args.get("name");
        var id = (Integer) args.get("id");
        var addons = (Addons) args.get("addons");
        return create(id, name, addons);
    }

    private Flavor create(Integer id, String name, Addons addons) {
        var flavor = new Flavor();
        flavor.setId(id);
        flavor.setName(name);
//        flavor.setAddons(addons);
        return flavor;
    }
}
