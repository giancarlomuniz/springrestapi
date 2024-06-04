package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import curso.api.rest.service.ImplementacaoUserDatailsService;

@Configuration
@EnableWebSecurity

public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements WebMvcConfigurer{

	@Autowired
	private ImplementacaoUserDatailsService implementacaoUserDatailsService ;
	
	
	/*Configura as solicitações de acesso por Http*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/*Ativando a proteção contra usuário que não estão validados por TOKEN*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Ativando a permissão para acesso a página incial do sistema EX: sistema.com.br/index*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index", "/recuperarSenha").permitAll()
		
		//.antMatchers(HttpMethod.OPTIONS, "/topicos").permitAll()
		.antMatchers(HttpMethod.GET, "/**","/usuario/recuperarSenha", "/usuario/**", "/possuiAcesso/**", "**/salvarCategoria").permitAll()
		.antMatchers(HttpMethod.POST,"/usuario/**", "/usuario/recuperarSenha", "/possuiAcesso/**", "**/salvarCategoria").permitAll()
		.antMatchers(HttpMethod.PUT, "/usuario/**", "**/salvarCategoria").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .anyRequest().authenticated() 
        //.and().formLogin() 
		
		/*URL de Logout - Redireciona após o user deslogar do sistema*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*Maperia URL de Logout e insvalida o usuário*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*Filtra requisições de login para autenticação*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
									UsernamePasswordAuthenticationFilter.class)
		
		/*Filtra demais requisições paa verificar a presenção do TOKEN JWT no HEADER HTTP*/
		.addFilterBefore(new JwtApiAutenticacacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	/*Service que irá consultar o usuário no banco de dados*/	
	auth.userDetailsService(implementacaoUserDatailsService)
	
	/*Padrão de codigição de senha*/
	.passwordEncoder(new BCryptPasswordEncoder());
	
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
    web.ignoring()
   .antMatchers(HttpMethod.GET,"/usuario/recuperarSenha", "/usuario/**",  "**/salvarCategoria", "/possuiAcesso/**")
   
    .antMatchers(HttpMethod.POST,"/usuario/recuperarSenha",  "/categoria/**", "**/salvarCategoria", "/possuiAcesso/**");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		 registry.addMapping("/**")
         .allowedOrigins("http://localhost:4200")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
	}
	


}
