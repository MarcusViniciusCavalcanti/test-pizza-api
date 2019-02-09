package com.test.api.pizza.domain.entity.size;

public enum SizePizza {
    SMALL {
        @Override
        public Size getSize() {
            return new Small();
        }
    },
    MIDDLE {
        @Override
        public Size getSize() {
            return new Middle();
        }
    },
    BIGGER {
        @Override
        public Size getSize() {
            return new Bigger();
        }
    };

    public abstract Size getSize();
}
