package de.adesso.mandelbrot.functions.images.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageData {
    private Integer[][] data;
    private int height;
    private int width;
    private int maxValue;

    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;
        data = new Integer[height][width];
    }
    public void setXY(int x, int y, Integer d) {
        data[y][x] = d;
    }

    public void setData(Integer[][] data, int h) {
        for (int y = 0; y < data.length; y++) {
            this.data[y + h] = new Integer[data[y].length];
            System.arraycopy(data[y],0, this.data[y + h], 0, data[y].length);
        }
    }
}
