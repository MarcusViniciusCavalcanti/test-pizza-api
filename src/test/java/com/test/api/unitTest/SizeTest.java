package com.test.api.unitTest;

import com.test.api.pizza.domain.entity.size.SizePizza;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SizeTest {

    @Test
    public void should_have_return_small() {
        var small = SizePizza.valueOf("SMALL").getSize();

        assertThat(small.value(), is(20.0));
        assertThat(small.timeProcess(), is(15));
    }

    @Test
    public void should_have_return_middle() {
        var small = SizePizza.valueOf("MIDDLE").getSize();

        assertThat(small.value(), is(30.0));
        assertThat(small.timeProcess(), is(20));
    }

    @Test
    public void should_have_return_bigger() {
        var small = SizePizza.valueOf("BIGGER").getSize();

        assertThat(small.value(), is(40.0));
        assertThat(small.timeProcess(), is(25));
    }
}
