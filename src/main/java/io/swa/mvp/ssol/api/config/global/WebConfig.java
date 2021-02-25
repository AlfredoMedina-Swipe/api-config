package io.swa.mvp.ssol.api.config.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import springfox.documentation.RequestHandler;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.ApiSelector;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
//@EnableSwagger2
//@EnableSwagger2WebMvc
public class WebConfig {

	@Value("${selful.cors.allowed.origins:*}")
	private List<String> origins;

	@Autowired
	private AuditRequestInterceptor auditRequestInterceptor;

	public String[] checkOrigins() {
		String arrayOrigins[];
		if (origins == null || origins.isEmpty()) {

			origins = Arrays.asList("*");
			arrayOrigins = new String[1];
			arrayOrigins[0] = origins.get(0);
		} else {
			arrayOrigins = new String[origins.size()];
			origins.toArray(arrayOrigins);
		}

		return arrayOrigins;
	}

	@Bean
	public WebMvcConfigurer corsContigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**").allowedOrigins(checkOrigins()).allowedMethods("*").allowedHeaders("*");

			}

			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(auditRequestInterceptor);
			}

//			@Override
//			public void addResourceHandlers(ResourceHandlerRegistry registry) {
//				registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//
//				registry.addResourceHandler("/webjars/**")
//						.addResourceLocations("classpath:/META-INF/resources/webjars/");
//			}

		};
	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.OAS_30).select().paths(PathSelectors.any())
//				.apis(RequestHandlerSelectors.any()).build();
//	}
}