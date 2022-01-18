package com.example.demo.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createDocket1() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo")).paths(PathSelectors.ant("/continent/**"))
				.build().apiInfo(apiInfo());
	}

	// only display content
	private ApiInfo apiInfo() {
		return new ApiInfo("Product Crud", "Product-Crud-App", "3.2GA", "http://productcrud.in",
				new Contact("Nikhil", "http://product.in", "sample -app by nik"), "Product Licence",
				"http://productcrud.in", List.of());
	}
}
