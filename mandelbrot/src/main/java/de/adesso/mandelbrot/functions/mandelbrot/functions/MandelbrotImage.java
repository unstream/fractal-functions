package de.adesso.mandelbrot.functions.mandelbrot.functions;

import de.adesso.mandelbrot.functions.images.entity.ImageData;
import de.adesso.mandelbrot.functions.mandelbrot.control.ImageServiceClient;
import de.adesso.mandelbrot.functions.mandelbrot.entity.Fractal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Function;

@Component
public class MandelbrotImage {

    private final static Logger LOG = LoggerFactory.getLogger(MandelbrotImage.class);

    @Autowired
    private Function<Fractal, ImageData> computeMandelbrotSetFunction;

    @Autowired
    private Function<ImageData, byte[]> createImageFunction;

    @Autowired
    private ImageServiceClient imageServiceClient;


    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation = @Operation(
                    description = "Generate a PNG image of the Mandelbrot set.", operationId = "computeMandelbrotImage",
                    tags = "mandelbrot")),
            @RouterOperation(method = RequestMethod.GET,operation = @Operation(operationId = "computeMandelbrotImage", hidden = true))
    })
    @Bean
    public Function<Fractal, byte[]> computeMandelbrotImage() {
        return computeMandelbrotSetFunction.andThen(createImageFunction);
    }

    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation = @Operation(
                    description = "Generate a PNG image of the Mandelbrot set. Call the create image function remotely",
                    tags = "mandelbrot")),
            @RouterOperation(method = RequestMethod.GET,operation = @Operation(hidden = true))
    })
    @Bean
    public Function<Fractal, byte[]> computeMandelbrotUsingRemoteImageService() {
        return fractal -> {
            return imageServiceClient.callCreateImage(computeMandelbrotSetFunction.apply(fractal));
        };
    }
}
