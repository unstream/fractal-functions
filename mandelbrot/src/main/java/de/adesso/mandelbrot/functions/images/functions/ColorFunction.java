package de.adesso.mandelbrot.functions.images.functions;


import de.adesso.mandelbrot.functions.images.control.Gradient;
import de.adesso.mandelbrot.functions.images.entity.Image;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ColorFunction {
    private final static Logger LOG = LoggerFactory.getLogger(ColorFunction.class);
    @Bean
    public Function<ImageData, Image> colorizeImageDate(){

        return imageData -> {
            long now = System.currentTimeMillis();
            Image image = new Image(imageData.getWidth(), imageData.getHeight());
            Gradient gradient = new Gradient(imageData.getMaxValue());

            for (int y = 0; y < imageData.getHeight(); y++) {
                for (int x = 0; x < imageData.getWidth(); x++) {
                    image.setColor(x, y, gradient.lookupColor(imageData.getData()[y][x]));
                }
            }
            LOG.info("Image colorized in " + (System.currentTimeMillis() -now) + " ms.");
            return image;
        };
    }

}

