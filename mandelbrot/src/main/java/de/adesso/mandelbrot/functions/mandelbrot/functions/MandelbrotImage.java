package de.adesso.mandelbrot.functions.mandelbrot.functions;

import de.adesso.mandelbrot.functions.images.entity.Image;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
import de.adesso.mandelbrot.functions.mandelbrot.control.ImageServiceClient;
import de.adesso.mandelbrot.functions.mandelbrot.entity.Fractal;
import io.swagger.v3.oas.annotations.Operation;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Function;

@Component
public class MandelbrotImage {
    private final Function<Fractal, ImageData> computeMandelbrotSetFunction;
    private final Function<ImageData, Image> colorizeFunction;
    private final Function<Image, byte[]> createImageFunction;
    private final ImageServiceClient imageServiceClient;

    @Autowired
    public MandelbrotImage(
            Function<ImageData, Image> colorizeFunction,
            Function<Image, byte[]> createImageFunction,
            Function<Fractal, ImageData> computeMandelbrotSetFunction,
            ImageServiceClient imageServiceClient) {
        this.colorizeFunction = colorizeFunction;
        this.createImageFunction = createImageFunction;
        this.computeMandelbrotSetFunction = computeMandelbrotSetFunction;
        this.imageServiceClient = imageServiceClient;
    }

    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation = @Operation(
                    description = "Generate a colorized PNG image of the Mandelbrot set.",
                    operationId = "computeMandelbrotImage",
                    tags = "mandelbrot")),
            @RouterOperation(method = RequestMethod.GET,operation = @Operation(
                    operationId = "computeMandelbrotImage",
                    hidden = true))
    })

    @Bean
    public Function<Fractal, byte[]> computeMandelbrotImage() {
        return fractal -> {
            computeMandelbrotSetFunction.apply(fractal);
            return new byte[0];
        };
        //return computeMandelbrotSetFunction.andThen(colorizeFunction).andThen(createImageFunction);
    }


    @RouterOperations(value = {
            @RouterOperation(method = RequestMethod.POST, operation = @Operation(
                    description = "Generate a PNG image of the Mandelbrot set. " +
                            "Call the create image function remotely",
                    tags = "mandelbrot")),
            @RouterOperation(method = RequestMethod.GET,operation = @Operation(hidden = true))
    })
    @Bean
    public Function<Fractal, byte[]> computeMandelbrotUsingRemoteImageService() {
        return fractal -> imageServiceClient.callCreateImage(computeMandelbrotSetFunction.apply(fractal));
    }
}
