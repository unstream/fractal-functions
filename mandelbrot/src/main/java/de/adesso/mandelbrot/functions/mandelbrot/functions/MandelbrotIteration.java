package de.adesso.mandelbrot.functions.mandelbrot.functions;

import de.adesso.mandelbrot.functions.mandelbrot.entity.IterationParameters;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Function;

@Component
public class MandelbrotIteration {

	@RouterOperations(value = {
			@RouterOperation(method = RequestMethod.POST, operation	= @Operation(
					description = "For the given point z iterate the Mandelbrot sequence until it reaches the given maximum number of iterations",
					tags = "mandelbrot")),
			@RouterOperation(method = RequestMethod.GET, operation = @Operation(hidden = true))
	})
	@Bean
	public Function<IterationParameters, Integer> completeIterationSequence() {
		return (parameters) -> {
			double zr = parameters.getCr();
			double zi = parameters.getCi();
			int maxIterations = parameters.getMaxIteration();
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
