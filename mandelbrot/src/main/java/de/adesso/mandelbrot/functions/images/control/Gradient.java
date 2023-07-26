package de.adesso.mandelbrot.functions.images.control;


import de.adesso.mandelbrot.functions.images.entity.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Gradient {
    public static final int MAP_SIZE = 1000;
    private int maxValue;
    private Map<Integer, Color> colorMap = new HashMap<>();

    public Gradient(Color color1, Color color2, int maxValue) {
        this.maxValue = maxValue;
        generateGradient(color1, color2);
    }

    public Gradient(int maxValue) {
        this.maxValue = maxValue;
        generateHSVGradient();
    }


    /**
     * Return the color for the iteration result, which is between 0 and maxValue.
     * @param value
     * @return
     */
    public Color lookupColor(int value) {
        if (value > maxValue) throw new IllegalArgumentException();
        return colorMap.get(MAP_SIZE * value / maxValue);
    }

    private void generateGradient(Color color1, Color color2) {
        int r1 = color1.getR();
        int g1 = color1.getB();
        int b1 = color1.getR();

        int r2 = color2.getR();
        int g2 = color2.getG();
        int b2 = color2.getB();
        for (int i = 0; i <= MAP_SIZE; i++) {
            var ratio = 1.0 * i / MAP_SIZE ;
            int r = (int) (r1 + ratio * (r2 - r1));
            int g = (int) (g1 + ratio * (g2 - g1));
            int b = (int) (b1 + ratio * (b2 - b1));
            colorMap.put(i, new Color(r, g, b));
        }
    }

    private void generateHSVGradient() {
        for (int m = 0; m < MAP_SIZE; m++) {
            int i = m * maxValue / MAP_SIZE;
            float h = (float) (240f * Math.pow(1f * i / maxValue, 1.5)) % 360;
            float s = 1f;
            float v = (float) Math.pow(1f * i / maxValue, 0.5);
            colorMap.put(m, ColorTools.HSVtoRGB(h, s, v));
        }
        colorMap.put(MAP_SIZE, ColorTools.HSVtoRGB(0, 0, 0));
    }
}

