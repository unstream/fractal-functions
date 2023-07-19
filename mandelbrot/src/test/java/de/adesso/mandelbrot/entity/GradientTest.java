package de.adesso.mandelbrot.entity;

import de.adesso.mandelbrot.functions.images.entity.Color;
import de.adesso.mandelbrot.functions.images.control.Gradient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradientTest {

    @Test
    void getColor() {
        Gradient gradient = new Gradient(new Color( 0, 0, 0), new Color(0,0,255), 2000);
        assertEquals(gradient.lookupColor(0).getB(), 0);
        assertEquals(255, gradient.lookupColor( 2000).getB());
    }
}
