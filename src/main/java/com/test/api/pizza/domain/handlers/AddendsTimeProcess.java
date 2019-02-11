package com.test.api.pizza.domain.handlers;

import com.test.api.pizza.application.validations.ConstraintAddons;
import com.test.api.pizza.domain.entity.addons.Addons;
import com.test.api.pizza.domain.entity.addons.AddonsTimeProcess;

public class AddendsTimeProcess extends AddonsRequestHandler {

    AddendsTimeProcess(AddonsRequestHandler handler) {
        super(handler);
    }

    public Addons handleRequest(String value) {
        if (ConstraintAddons.checkTime(value)) {
            var addendsTimeProcess = new AddonsTimeProcess();

            var hours = Integer.valueOf(value.substring(0, 2));
            var minute = Integer.valueOf(value.substring(3));

            var time = (hours * 60) + minute;

            addendsTimeProcess.setTime(time);

            return addendsTimeProcess;
        }
        return super.handleRequest(value);
    }
}
