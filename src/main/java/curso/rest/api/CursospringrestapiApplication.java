package curso.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EntityScan(basePackages = {"curso.api.rest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
	}


	 /*Mapeamento Global que refletem em todo o sistema*/
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			/*Liberando o mapeamento de usuario para todas as origens*/
			registry.addMapping("/**")
			.allowedMethods("*")
			.allowedOrigins("*")
			.allowedHeaders("*");
			
			/*Liberando o mapeamento de Recuperar para todas as origens*/
			registry.addMapping("/usuario/**")
			.allowedMethods("*")
			.allowedOrigins("*");
			
			
			/*Liberando o mapeamento de Profesores para todas as origens*/
			registry.addMapping("/usuario/professor/**")
			.allowedMethods("*")
			.allowedOrigins("*");
		
			/*Liberando o mapeamento de Categoria para todas as origens*/
			registry.addMapping("/categoria/**")
			.allowedMethods("*")
			.allowedOrigins("*");
			
			/*Liberando o mapeamento de Recuperar para todas as origens*/
			registry.addMapping("usuario/recuperarSenha")
			.allowedMethods("*")
			.allowedOrigins("*");
			
			/*Liberando o mapeamento de PossuiAcesso para todas as origens*/
			registry.addMapping("/possuiAcesso/{username}/{role}/**")
			.allowedMethods("*")
			.allowedOrigins("*")
			.allowedHeaders("*");
			
			
			 registry.addMapping("/**")
	         .allowedOrigins("http://localhost:4200")
	        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
			
		}
		public CorsFilter corsFilter() {
			 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		        CorsConfiguration config = new CorsConfiguration();

		        // Defina as origens permitidas (por exemplo, http://localhost:4200)
		        config.addAllowedOrigin("http://localhost:4200");

		        // Defina os métodos HTTP permitidos (GET, POST, etc.)
		        config.addAllowedMethod("GET");
		        config.addAllowedMethod("POST");

		        // Defina os cabeçalhos permitidos (Authorization, Content-Type, etc.)
		        config.addAllowedHeader("Authorization");
		        config.addAllowedHeader("Content-Type");

		        source.registerCorsConfiguration("/**", config);
		        return new CorsFilter(source);
			
		}
		

}
