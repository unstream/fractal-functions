package de.adesso.mandelbrot.functions.images.functions;


import de.adesso.mandelbrot.functions.images.control.Gradient;
import de.adesso.mandelbrot.functions.images.entity.Image;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Function;

@Component
public class ColorFunction {
    private final static Logger LOG = LoggerFactory.getLogger(ColorFunction.class);

    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation	= @Operation(
                    description = "Generate a colorized image for the given image data.",
                    tags = "images")),
            @RouterOperation(method = RequestMethod.GET, operation = @Operation(hidden = true))
    })    @Bean
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

