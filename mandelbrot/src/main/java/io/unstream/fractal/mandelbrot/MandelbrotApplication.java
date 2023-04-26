package io.unstream.fractal.mandelbrot;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class MandelbrotApplication {

	@Bean
	public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI()
				.components(new Components())
				//.addServersItem(new Server().description("MyServer").url("http://localhost:8080/functions/"))
				.path("functions", new PathItem())
				.info(new Info().title("Mandelbrot API").version(appVersion)
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

	public static void main(String[] args) {
		SpringApplication.run(MandelbrotApplication.class, args);
	}

}
