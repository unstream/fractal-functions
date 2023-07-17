package io.unstream.fractal.mandelbrot.functions;

import io.unstream.fractal.mandelbrot.entity.MandelbrotIterationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MandelbrotIteration {
	public static int iterateZ2(double zr, double zi, int maxIterations) {
		int i = 0;
		double x = 0, y = 0;
		double x2, y2;
		do {
			x2 = x * x;
			y2 = y * y;
			y = 2 * x * y + zi;
			x = x2 - y2 + zr;
			i++;
		} while (i < maxIterations && (x2 + y2) < 4);
		return i;
	};

	@Bean
	public Function<MandelbrotIterationDTO, Integer> iterateZ() {
		return (dto) -> {
			double zr = dto.getZ().getReal();
			double zi = dto.getZ().getImaginary();
			int maxIterations = dto.getMaxIteration();
			int i = 0;
			double x = 0, y = 0;
			double x2, y2;
			do {
				x2 = x * x;
				y2 = y * y;
				y = 2 * x * y + zi;
				x = x2 - y2 + zr;
				i++;
			} while (i < maxIterations && (x2 + y2) < 4);
			return i;
		};
	}

}
