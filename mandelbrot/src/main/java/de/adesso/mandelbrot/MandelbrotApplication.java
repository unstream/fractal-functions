package de.adesso.mandelbrot;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MandelbrotApplication {

	@Bean
	public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI()
				.components(new Components())
				.path("functions", new PathItem())
				.info(new Info().title("Mandelbrot API").version(appVersion)
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
	public static void main(String[] args) {
		SpringApplication.run(MandelbrotApplication.class, args);
	}

}

