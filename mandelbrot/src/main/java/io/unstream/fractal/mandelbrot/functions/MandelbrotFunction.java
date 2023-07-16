package io.unstream.fractal.mandelbrot.functions;

import io.unstream.fractal.mandelbrot.entity.Fractal;
import io.unstream.fractal.mandelbrot.entity.Quad;
import org.apache.commons.math3.complex.Complex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author Ingo Weichsel
 */
@Component
public class MandelbrotFunction {

    private final static Logger LOG = LoggerFactory.getLogger(MandelbrotFunction.class);

    public MandelbrotFunction() {
    }

    @Bean
    public Function<Fractal, Quad> mandelbrotQuad(){
        return fractal -> {
            long now = System.currentTimeMillis();
            MandelbrotIteration f = new MandelbrotIteration(fractal.getMaxIterations());

            var data = new Quad(fractal.getWidth(), fractal.getHeight());
            data.setMaxValue(fractal.getMaxIterations());
            var width = fractal.getC1() - fractal.getC0();
            var height = fractal.getC1i() - fractal.getC0i();
            var xstep = width / fractal.getWidth();
            var ystep = height / fractal.getHeight();

            var executor = Executors.newFixedThreadPool(20);
            try  { //newVirtualThreadPerTaskExecutor() newFixedThreadPool(20)
                IntStream.rangeClosed(0, fractal.getWidth() - 1).forEach(x -> {
                    executor.execute(() -> {
                        var z0 = xstep * x + fractal.getC0();
                        for (int y = 0; y < fractal.getHeight(); y++) {
                            var z = new Complex(z0, ystep * y + fractal.getC0i());
                            var r = f.apply(z);
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
