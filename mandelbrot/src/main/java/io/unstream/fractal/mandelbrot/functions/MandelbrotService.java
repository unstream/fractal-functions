package io.unstream.fractal.mandelbrot.functions;

import io.unstream.fractal.mandelbrot.entity.Fractal;
import io.unstream.fractal.mandelbrot.entity.Quad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.function.Function;

/**
 * @author Ingo Weichsel
 */
@Component
public class MandelbrotService {

    private final static Logger LOG = LoggerFactory.getLogger(MandelbrotService.class);
    @Value("${image-service-url}")
    private String imageServiceUrl;

    public MandelbrotService() {
    }



    @Autowired
    Function<Fractal, Quad> mandelbrotFunction;

//    @Bean
//    @RouterOperations({
//            @RouterOperation(method = RequestMethod.GET, operation = @Operation(description = "Compute a mandelbrot image", operationId = "mandelbrotGET", tags = "positions",
//                    responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Quad.class))))),
//            @RouterOperation(method = RequestMethod.POST, operation = @Operation(description = "Compute a mandelbrot image", operationId = "mandelbrotPOST", tags = "positions",
//                    responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Quad.class)))))
//    })
    @Bean
    public Function<Fractal, byte[]> mandelbrotPng(){
        return fractal -> {
            Quad quad = mandelbrotFunction.apply(fractal);
            LOG.info("Calling " + imageServiceUrl);
            WebClient client = WebClient.builder()
                    .baseUrl(imageServiceUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/computePng"))
                    .build();
            byte[] data = client
                    .post()
                    //.headers(h -> h.setBearerAuth(SecurityContextHolder.getContext() ))
                    .bodyValue(quad)
                    .accept(MediaType.IMAGE_PNG)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            return data;
        };
    }
}
