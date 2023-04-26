package io.unstream.fractal.mandelbrot.functions;

import io.unstream.fractal.mandelbrot.entity.Fractal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileFunctionsTest {

    @Test
    void computeTilesTest1() {
        TileFunctions tf  = new TileFunctions();
        Fractal fractal = new Fractal();
        fractal.setC0(1.1);
        fractal.setC0i(0.1);
        fractal.setC1(1.2);
        fractal.setC1i(0.1);
        long tile = tf.computeTile().apply(fractal);
        assertEquals(6574, tile);
    }

    @Test
    void computeTilesTest2() {
        TileFunctions tf  = new TileFunctions();
        Fractal fractal = new Fractal();
        fractal.setC0(-2);
        fractal.setC0i(-2);
        fractal.setC1(-1);
        fractal.setC1i(-1);
        long tile = tf.computeTile().apply(fractal);
        assertEquals(15, tile);
    }
    @Test
    void computeTilesTest3() {
        TileFunctions tf  = new TileFunctions();
        Fractal fractal = new Fractal();
        fractal.setC0(-2);
        fractal.setC0i(-2);
        fractal.setC1(0);
        fractal.setC1i(0);
        long tile = tf.computeTile().apply(fractal);
        assertEquals(1, tile);
    }
}
