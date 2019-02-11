package com.test.api.pizza.domain.handlers;

import com.test.api.pizza.application.validations.ConstraintAddons;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.pizza.domain.entity.addons.AddonsPrice;

public class AddendsPrice extends AddonsRequestHandler {

    AddendsPrice(AddonsRequestHandler next) {
        super(next);
    }

    public Addons handleRequest(String value) {
        if (ConstraintAddons.checkMonetaryValue(value)) {
          var addons = new AddonsPrice();
            String replace = value.replace(".", "");
            String replace1 = replace.replace(",", ".");
            var price = Double.valueOf(replace1);

          addons.setPrice(price);

          return  addons;
        }
        return super.handleRequest(value);
    }
}
