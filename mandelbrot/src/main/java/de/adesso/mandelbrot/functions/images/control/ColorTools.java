package de.adesso.mandelbrot.functions.images.control;


import de.adesso.mandelbrot.functions.images.entity.Color;

public class ColorTools {

    private ColorTools() {
    }

    public static float[] RGBtoHSV(int red, int green, int blue) {
        float[] hsv = new float[3];
        float r = red / 255f;
        float g = green / 255f;
        float b = blue / 255f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        // Hue
        if (max == min) {
            hsv[0] = 0;
        } else if (max == r) {
            hsv[0] = ((g - b) / delta) * 60f;
        } else if (max == g) {
            hsv[0] = ((b - r) / delta + 2f) * 60f;
        } else if (max == b) {
            hsv[0] = ((r - g) / delta + 4f) * 60f;
        }

        // Saturation
        if (delta == 0)
            hsv[1] = 0;
        else
            hsv[1] = delta / max;

        //Value
        hsv[2] = max;

        return hsv;
    }


    public static Color HSVtoRGB(float hue, float saturation, float value) {
        Color color = new Color();
        float hi = (float) Math.floor(hue / 60.0) % 6;
        float f = (float) ((hue / 60.0) - Math.floor(hue / 60.0));
        float p = (float) (value * (1.0 - saturation));
        float q = (float) (value * (1.0 - (f * saturation)));
        float t = (float) (value * (1.0 - ((1.0 - f) * saturation)));

        if (hi == 0) {
            color.setR((int) (value * 255));
            color.setG((int) (t * 255));
            color.setB((int) (p * 255));
        } else if (hi == 1) {
            color.setR((int) (q * 255));
            color.setG((int) (value * 255));
            color.setB((int) (p * 255));
        } else if (hi == 2) {
            color.setR((int) (p * 255));
            color.setG((int) (value * 255));
            color.setB((int) (t * 255));
        } else if (hi == 3) {
            color.setR((int) (p * 255));
            color.setG((int) (value * 255));
            color.setB((int) (q * 255));
        } else if (hi == 4) {
            color.setR((int) (t * 255));
            color.setG((int) (value * 255));
            color.setB((int) (p * 255));
        } else if (hi == 5) {
            color.setR((int) (value * 255));
            color.setG((int) (p * 255));
            color.setB((int) (q * 255));
        }
        return color;
    }
}
