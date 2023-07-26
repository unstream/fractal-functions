package de.adesso.mandelbrot.functions.mandelbrot.entity;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fractal {
  private double c0;
  private double c0i;
  private double c1;
  private double c1i;
  private int width;
  private int height;
  private int maxIterations;
}
