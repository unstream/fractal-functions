package de.adesso.mandelbrot.functions.images.control;


import de.adesso.mandelbrot.functions.images.entity.Color;

public class Gradient {
    public static final int MAP_SIZE = 1000;
    private int maxValue;
    private Color[] colorMap = new Color[MAP_SIZE + 1];

    public Gradient(Color color1, Color color2, int maxValue) {
        this.maxValue = maxValue;
        generateGradient(color1, color2);
    }

    /**
     * Return the color for the fractal value, wich is between 0 and maxValue.
     * @param value
     * @return
     */
    public Color lookupColor(int value) {
        if (value > maxValue) throw new IllegalArgumentException();
        return colorMap[value * MAP_SIZE / (maxValue)];
    }

    private void generateGradient(Color color1, Color color2) {
        int r1 = color1.getR();
        int g1 = color1.getB();
        int b1 = color1.getR();

        int r2 = color2.getR();
        int g2 = color2.getG();
        int b2 = color2.getB();

        for (int i = 0; i <= MAP_SIZE; i++) {
            float ratio = (float)  i / (MAP_SIZE);
            int r = (int) (r1 + ratio * (r2 - r1));
            int g = (int) (g1 + ratio * (g2 - g1));
            int b = (int) (b1 + ratio * (b2 - b1));
            colorMap[i] = new Color(r, g,  b);
        }
    }
}

