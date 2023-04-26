package io.unstream.fractal.mandelbrot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Quad {
    private Integer[][] data;
    private int height;
    private int width;

    public Quad(int width, int height) {
        this.width = width;
        this.height = height;
        data = new Integer[width][height];
    }
    public void setXY(int x, int y, Integer d) {
        data[x][y] = d;
    }
}
