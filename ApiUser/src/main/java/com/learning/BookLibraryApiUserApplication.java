package com.learning;

import static com.learning.constant.FileConstant.USER_FOLDER;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import feign.Logger;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class BookLibraryApiUserApplication {

	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(BookLibraryApiUserApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token",
				"Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		corsConfiguration.setMaxAge((long) 3600);
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	@LoadBalanced
//	public RestTemplate getRestTemplate() {
//		return new RestTemplate();
//	}

	@Bean
	@Profile("production")
	Logger.Level feignLoggerLover() {
		return Logger.Level.NONE;
	}

	@Bean
	@Profile("!production")
	Logger.Level feignDefaultLoggerLover() {
		return Logger.Level.FULL;
	}

	/*
	 * @Bean public FeignErrorDecoder getFeignErrorDecoder() { return new
	 * FeignErrorDecoder(); }
	 */

	@Bean
	public Throwable getThrowable() {
		return new Throwable();
	}

	@Bean
	@Profile("production")
	public String createProductionBean() {
		System.out.println(
				"Production Bean Created. myapplication.environment = " + env.getProperty("myapplication.environment"));
		return "Production Bean";
	}

	@Bean
	@Profile("!production")
	public String createNotProductionBean() {
		System.out.println("Not Production Bean Created. myapplication.environment = "
				+ env.getProperty("myapplication.environment"));
		return "Not Production Bean";
	}

	@Bean
	@Profile("default")
	public String createDefaultBean() {
		System.out.println("Development Bean Created. myapplication.environment = "
				+ env.getProperty("myapplication.environment"));
		return "Development Bean";
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${app.description}") String appDescription,
			@Value("${app.version}") String appVersion) {
		return new OpenAPI().info(new Info().title("Course Tracker API").version(appVersion)
				.description(appDescription).termsOfService("http://swagger.io/terms/")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
