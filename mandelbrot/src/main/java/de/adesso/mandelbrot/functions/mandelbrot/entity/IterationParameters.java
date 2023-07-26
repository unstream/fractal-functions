package de.adesso.mandelbrot.functions.mandelbrot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.apache.commons.math3.complex.Complex;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IterationParameters {
    double cr;
    double ci;
    int maxIteration;
}
