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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author Ingo Weichsel
 */
@Component
public class FractalService {

    private final static Logger LOG = LoggerFactory.getLogger(FractalService.class);
    public static final int SLICE_HEIGHT = 50;
    @Value("${image-service-url}")
    private String imageServiceUrl;

    @Value("${mandelbrot-service-url}")
    private String mandelbrotServiceUrl;
    private ForkJoinPool forkJoinPool;

    public FractalService() {
        forkJoinPool = new ForkJoinPool(8);
    }

    @Autowired
    Function<Fractal, Quad> mandelbrotFunction;

    @Bean
    public Function<Fractal, byte[]> createFractal(){
        return fractal -> {

            var quad = new Quad(fractal.getWidth(), fractal.getHeight());
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (int i = 0; i < fractal.getHeight() / SLICE_HEIGHT; i++) {
                Fractal f = getFractalSlice(fractal, i);
                final int currentHeight = i * SLICE_HEIGHT;
                final int fi = i;
                futures.add(CompletableFuture.runAsync(() -> {
                    long start = System.currentTimeMillis();
                    Quad q = mandelbrotQuad(f);
                    quad.setData(q.getData(), currentHeight);
                    LOG.info("Needed " + (System.currentTimeMillis() - start) + " ms for slice " + fi + ".");
                }, forkJoinPool));
            }
            CompletableFuture<Void> allFutures = CompletableFuture
                    .allOf(futures.toArray(new CompletableFuture[futures.size()]));
            try {
                allFutures.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            //futures.forEach(CompletableFuture::join);

            long start = System.currentTimeMillis();

            byte[] imageData = createPng(quad);
            LOG.info("Needed " + (System.currentTimeMillis() - start) + " ms for image creation.");

            return imageData;
        };
    }

    private static Fractal getFractalSlice(Fractal fractal, int i) {
        var yStep = (fractal.getC1i() - fractal.getC0i()) / fractal.getHeight() ;
        double c0i = fractal.getC0i() + i * yStep * SLICE_HEIGHT;
        Fractal f;
        f = new Fractal();
        f.setC0(fractal.getC0());
        f.setC1(fractal.getC1());
        f.setC1i(fractal.getC1i());
        f.setC0i(c0i);
        double c1i =  c0i + yStep * SLICE_HEIGHT;
        if (c1i <= fractal.getC1i()) {
            f.setC1i(c1i);
            f.setHeight(50);
        } else {
            f.setC1i(fractal.getC1i());
            f.setHeight(fractal.getHeight() % SLICE_HEIGHT);
        }
        f.setWidth(fractal.getWidth());
        f.setMaxIterations(fractal.getMaxIterations());
        return f;
    }

    private Quad mandelbrotQuad(Fractal fractal) {
        LOG.info("Calling " + mandelbrotServiceUrl + " for " + fractal.getHeight() + " lines.");
        WebClient client = WebClient.builder()
                .baseUrl(mandelbrotServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080/mandelbrotQuad"))
                .build();
        Quad quad = client
                .post()
                //.headers(h -> h.setBearerAuth(SecurityContextHolder.getContext() ))
                .bodyValue(fractal)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Quad.class)
                .block();
        LOG.info("Done");
        return quad;
    }


    private byte[] createPng(Quad quad) {
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
    }
}
