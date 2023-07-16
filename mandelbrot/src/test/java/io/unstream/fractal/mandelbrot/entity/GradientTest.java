package io.unstream.fractal.mandelbrot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradientTest {

    @Test
    void getColor() {
        Gradient gradient = new Gradient(new Color( 0, 0, 0), new Color(0,0,255), 2000);
        assertEquals(gradient.getColor(0).getB(), 0);
        assertEquals(255, gradient.getColor( 2000).getB());
    }
}
