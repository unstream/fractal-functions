package de.adesso.mandelbrot.functions.images.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image {
    private Color[][] data;
    private int height;
    private int width;
    private int maxValue;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        data = new Color[height][width];
    }

    public void setColor(int x, int y, Color color) {
        data[y][x] = color;
    }
}
