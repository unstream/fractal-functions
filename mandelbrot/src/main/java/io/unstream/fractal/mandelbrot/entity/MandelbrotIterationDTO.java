package io.unstream.fractal.mandelbrot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MandelbrotIterationDTO {
    Complex z;
    Integer maxIteration;
}
