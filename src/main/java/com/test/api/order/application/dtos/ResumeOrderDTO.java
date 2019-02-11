package com.test.api.order.application.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ResumeOrderDTO {

    private Long orderId;

    private OrderCloseDetailsSize sizeDetails;

    private List<OrderCloseDetailsCustomizations> customizationsDetails;

    private Double total;

    private Integer timeProcess;

    @Data
    public static class OrderCloseDetailsSize {
        private String size;
        private Double value;
    }

    @Data
    public static class OrderCloseDetailsCustomizations {
        private String name;
        private Double value;
    }

}


