package de.adesso.mandelbrot.functions.mandelbrot.functions;

import de.adesso.mandelbrot.functions.images.entity.ImageData;
import de.adesso.mandelbrot.functions.images.functions.ImageFunction;
import de.adesso.mandelbrot.functions.mandelbrot.entity.Fractal;
import de.adesso.mandelbrot.functions.mandelbrot.entity.IterationParameters;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class MandelbrotData {

    private final static Logger LOG = LoggerFactory.getLogger(MandelbrotData.class);

    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation	= @Operation(
                    description = "Compute the mandelbrot set data with the iteration count per set point",
                    tags = "mandelbrot")),
            @RouterOperation(method = RequestMethod.GET, operation = @Operation(hidden = true))
    })@Bean
    public Function<Fractal, ImageData> computeMandelbrotSet(){
        return fractal -> {
            long now = System.currentTimeMillis();

            var data = new ImageData(fractal.getWidth(), fractal.getHeight());
            data.setMaxValue(fractal.getMaxIterations());

            MandelbrotIteration mandelbrotIteration = new MandelbrotIteration();
            Function<IterationParameters, Integer> iterateZ = mandelbrotIteration.completeIterationSequence();

            var width = fractal.getC1() - fractal.getC0();
            var height = fractal.getC1i() - fractal.getC0i();
            var xstep = width / fractal.getWidth();
            var ystep = height / fractal.getHeight();

            var executor = Executors.newFixedThreadPool(25);

            try  {
                IntStream.rangeClosed(0, fractal.getWidth() - 1).forEach(x -> {
                    executor.execute(() -> {
                        var zr = xstep * x + fractal.getC0();
                        for (int y = 0; y < fractal.getHeight(); y++) {
                            var zi = ystep * y + fractal.getC0i();
                            var r = iterateZ.apply(new IterationParameters(zr, zi , fractal.getMaxIterations()));
                            data.setXY(x, y, r);
                        }
                    });
                });
            } catch (RuntimeException re) {
                LOG.error(re.getMessage(), re);
            } finally {
                try {
                    executor.shutdown();
                    executor.awaitTermination(600, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            LOG.info("Fractal computed in " + (System.currentTimeMillis() -now) + " ms.");
            return data;
        };
    }
}
