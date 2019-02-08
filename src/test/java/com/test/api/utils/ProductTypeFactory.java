package com.test.api.utils;

import com.test.api.product.domain.assemble.ProductTypeAssemble;
import com.test.api.product.domain.entity.ProductType;
import com.test.api.product.domain.resource.ProductTypeResource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductTypeFactory extends FactoryObjectsToTest<ProductType> {
    public static final int ID_SMALL = 1;
    public static final int ID_MIDDLE = 2;
    public static final int ID_BIGGER = 3;
    public static final int ID_SAVE = 4;

    public static final String NAME_SMALL = "pequena";
    public static final String NAME_MIDDLE = "média";
    public static final String NAME_BIGGER = "grande";
    public static final String NAME_SAVE = "SAVE";
    public static final int ID_NOT_FOUND = 10000;

    public static final int MINUTE_15 = 15;
    public static final int MINUTE_20 = 20;
    public static final int MINUTE_25 = 25;

    @Override
    protected ProductType createSample() {
        return create(ID_SMALL, NAME_SMALL, MINUTE_15);
    }

    @Override
    protected List<ProductType> createList() {
        return createThreeObjects();
    }

    @Override
    protected ProductType createBy(Map<String, Object> args) {
        var name = (String) args.get("name");
        var id = (Integer) args.get("id");
        var time = (Integer) args.get("time");

        return create(id, name, time);
    }

    private static ProductType create(Integer id, String name, int timeProcessor) {
        var productType = new ProductType();
        productType.setId(id);
        productType.setName(name);
        productType.setTimeProcess(timeProcessor);
        return productType;
    }

    private static List<ProductType> createThreeObjects() {
        var small = create(ID_SMALL, NAME_SMALL, MINUTE_15);
        var middle = create(ID_MIDDLE, NAME_MIDDLE, MINUTE_20);
        var bigger = create(ID_BIGGER, NAME_BIGGER, MINUTE_25);

        return Arrays.asList(small, middle, bigger);
    }

    private static List<ProductTypeResource> createThreeResources() {
        var assemble = new ProductTypeAssemble();
        var list = createThreeObjects();
        return assemble.toResources(list);
    }

}
