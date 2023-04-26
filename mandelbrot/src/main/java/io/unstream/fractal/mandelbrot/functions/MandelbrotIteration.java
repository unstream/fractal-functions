package io.unstream.fractal.mandelbrot.functions;

import org.apache.commons.math3.complex.Complex;

import java.util.function.Function;

public class MandelbrotIteration implements Function<Complex, Integer>  {
	final private int maxIterations;
	
	public MandelbrotIteration(final int maxIterations) {
		this.maxIterations = maxIterations;
	}
	
	@Override
	public Integer apply(Complex z) {
		int i = 0;
		double x = 0;
		double y = 0;
		double x2, y2;
		do {
			x2 = x * x;
			y2 = y * y;
			y = 2 * x * y + z.getImaginary();
			x = x2 - y2 + z.getReal();
			i++;
		} while (i < maxIterations && (x2 + y2) < 4);
		return i;
//		double nsmooth;
//		if (i == maxIterations) {
//			nsmooth = i;
//		} else {
//			//nsmooth = 1d + i - Math.log(Math.log(Math.sqrt(x*x + y*y)))/Math.log(2);
//			nsmooth = i;
//		}
//		return nsmooth;
	}
}
