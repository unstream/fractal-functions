package de.adesso.mandelbrot.functions.mandelbrot.control;

import de.adesso.mandelbrot.functions.mandelbrot.entity.Fractal;
import de.adesso.mandelbrot.functions.images.entity.ImageData;
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
public class ImageServiceClient {

    private final static Logger LOG = LoggerFactory.getLogger(ImageServiceClient.class);
    @Value("${image-service-url}")
    private String imageServiceUrl;


    public byte[] callCreateImage(ImageData imageData) {
        LOG.info("Calling " + imageServiceUrl);
        WebClient client = WebClient.builder()
                .baseUrl(imageServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/computePng"))
                .build();
        byte[] data = client
                .post()
                .bodyValue(imageData)
                .accept(MediaType.IMAGE_PNG)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        return data;
    }
}
