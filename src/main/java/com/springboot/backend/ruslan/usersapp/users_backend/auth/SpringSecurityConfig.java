package com.springboot.backend.ruslan.usersapp.users_backend.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.springboot.backend.ruslan.usersapp.users_backend.auth.filter.JWTAuthenticationFilter;
import com.springboot.backend.ruslan.usersapp.users_backend.auth.filter.JWTValidationFilter;


@Configuration
public class SpringSecurityConfig {
    
    @Autowired
    private AuthenticationConfiguration AuthenticationConfiguration;

    /**
     * Este metodo nos permite poder obtener el componente AuthenticationManager que es un componente central en Spring Security
     * que se encarga de autenticar las credenciales del usuario.
     * Este bean se obtiene llamando al método getAuthenticationManager() de authConfiguration, 
     * que es una instancia de una clase de configuración de seguridad. 
     * @return nos devuelve el AuthenticationManager de la configuración de seguridad.
     * @throws Exception si hay un error en la configuración de seguridad.
     * Al ser un componente @Bean, Spring lo gestiona y lo inyecta en otras clases.
     * Además de obtener el AuthenticationManager, también se configura el filtro de seguridad mas adelante.
     */
    @Bean 
    AuthenticationManager authenticationManager() throws Exception {
        return AuthenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Método que nos permite encriptar la contraseña de los usuarios.
     * Creamos un objeto de la clase BCryptPasswordEncoder la devolvemos y con @Bean le decimos a Spring que lo gestione en el contenedor de beans.
     * @return devuelve un objeto de la clase BCryptPasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Se crea un objeto de la clase BCryptPasswordEncoder con la configuración por defecto.
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(authz ->
        authz
        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/{users}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
        .anyRequest().authenticated())
        .cors(cors -> cors.configurationSource(configurationSource()))
        .addFilter(new JWTAuthenticationFilter(authenticationManager())) // Se añade el filtro de autenticación JWT
        .addFilter(new JWTValidationFilter(authenticationManager())) // Se añade el filtro de validación del token JWT
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }
    
    @Bean 
    CorsConfigurationSource configurationSource(){

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); 
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); 
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(null);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

     }

     @Bean
     FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(
            new CorsFilter(this.configurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
     }

}
