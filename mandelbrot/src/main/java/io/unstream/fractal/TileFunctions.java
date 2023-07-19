package io.unstream.fractal;

import de.adesso.mandelbrot.functions.mandelbrot.entity.Fractal;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author Ingo Weichsel
 */
@Component
@Slf4j
public class TileFunctions {
    public TileFunctions() {
    }

    /**
     * Compute the tile number of the smallest tile containing the complete fractal:
     * L0: t0: (-2,-2) - (2,2)
     * L1: t5: (-0.5,-0.5) - (0.5-0.5)
     * L2: t55: (-0.125,-0.125) - (0.125-0.125)
     * L3: t555: (-0.04125,-0.04125) - (0.04125-0.04125)
     *
     * L0: 0
     * L1:  1 2 3
     *      4 5 6
     *      7 8 9
     * L2:  11 12 13 21 22 23 ...
     *      14 15 16 ...
     *      16 17 18 ...
     *      41 ...
     *      ...
     *      77 ... 99
     *
     * L3:  111 ... 333
     *      ...
     *      777 ... 999
     *
     * Tile no x consists of the tiles x*10+1, ... , x*10+9
     * @return
     */
    @Bean
    public Function<Fractal, Long> computeTile2(){
        return fractal -> {
            //tw = 5; w = 7; there a 3 scenarios
            // -t1-  -t2-  -t3-
            // ................
            // xxxxxxx.........
            // ....xxxxxxx.....
            // ........xxxxxxx.
            // If the fractal width is just smaller then the tile width, the the fractal fits in 4-9 tiles
            //
            var data = new ImageData(fractal.getWidth(), fractal.getHeight());
            var width = fractal.getC1() - fractal.getC0();
            var height = fractal.getC1i() - fractal.getC0i();
            var max = Math.max(width, height);
            var tw = 4.0;
            var level = 0;
            long tile = 0;
            double middleX = (fractal.getC0() + width / 2.0);
            double middleY = (fractal.getC0i() + width / 2.0);
            while (tw > max) {

                tw /= 3.0;
                level++;
                long d;
                if (middleX < -tw / 2.0) {
                    d = 1;
                    middleX += tw;
                } else if (middleX > tw / 2.0) {
                    d = 3;
                    middleX -= tw;
                } else {
                    d = 2;
                }
                if (middleY < -tw / 2.0) {
                    middleY += tw;
                } else if (middleY > tw / 2.0) {
                    d += 6;
                    middleY -= tw;
                } else {
                    d += 3;
                }
                tile = tile * 10 + d;
                log.info("level=" + level + " tile=" + tile );
            }
            log.info("tw = " + tw );
            return tile;
        };
    }

    /**
     * Compute the tile number of the smallest tile containing the complete fractal:
     * L0: t0: (-2,-2) - (2,2)
     * L1: t5: (-0.5,-0.5) - (0.5-0.5)
     * L2: t55: (-0.125,-0.125) - (0.125-0.125)
     * L3: t555: (-0.04125,-0.04125) - (0.04125-0.04125)
     *
     * L0: 0
     * L1:  1 2 3
     *      4 5 6
     *      7 8 9
     * L2:  10 11 ... 17 18
     *      19 20 ... 26 27
     *      ...
     *      ...
     *      81 82 ... 88 89
     *
     * Each level consists of (3^level)^2 tiles. They are numbered line after line.
     * @return
     */
    @Bean
    public Function<Fractal, Long> computeTile(){
        return fractal -> {
            //tw = 5; w = 7; there a 3 scenarios
            // -t1-  -t2-  -t3-
            // ................
            // xxxxxxx.........
            // ....xxxxxxx.....
            // ........xxxxxxx.
            // If the fractal width is just smaller then the tile width, the the fractal fits in 4-9 tiles
            //
            var data = new ImageData(fractal.getWidth(), fractal.getHeight());
            var width = fractal.getC1() - fractal.getC0();
            var height = fractal.getC1i() - fractal.getC0i();
            var max = Math.max(width, height);
            var tw = 4.0;
            var level = 0;
            long tile = 0;
            double middleX = (fractal.getC0() + width / 2.0);
            double middleY = (fractal.getC0i() + width / 2.0);
            while (tw > max) {

                tw /= 3.0;
                level++;
                long d;
                if (middleX < -tw / 2.0) {
                    d = 1;
                    middleX += tw;
                } else if (middleX > tw / 2.0) {
                    d = 3;
                    middleX -= tw;
                } else {
                    d = 2;
                }
                if (middleY < -tw / 2.0) {
                    middleY += tw;
                } else if (middleY > tw / 2.0) {
                    d += 6;
                    middleY -= tw;
                } else {
                    d += 3;
                }
                tile = tile * 10 + d;
                log.info("level=" + level + " tile=" + tile );
            }
            log.info("tw = " + tw );
            return tile;
        };
    }

    /**
     * Inverse Operation of computeTile.
     * @return
     */
    @Bean
    public Function<Long, Fractal> computeFractal() {
        return tileNo -> {
            Fractal f = new Fractal();

            return f;
        };
    }

}
