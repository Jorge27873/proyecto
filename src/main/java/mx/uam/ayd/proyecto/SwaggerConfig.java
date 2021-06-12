package mx.uam.ayd.proyecto;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * Configura el swagger
 * 
 *
 * para acceder al api: http://localhost:8080/swagger-ui.html
 * 
 * 
 */
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2

public class SwaggerConfig {
	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2)  
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());                                           
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "API REST Proyecto Final TSIS", 
	      "Accede a los endpoints del API del proyecto", 
	      "API TOS", 
	      "Terms of service", 
	      new Contact("Equipo", "www.uam.mx", "lista de contactos"), 
	      "License of API", "API license URL", Collections.emptyList());
    }
}