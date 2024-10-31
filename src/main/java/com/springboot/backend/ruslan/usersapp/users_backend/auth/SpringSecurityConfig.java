package com.springboot.backend.ruslan.usersapp.users_backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.backend.ruslan.usersapp.users_backend.auth.filter.JWTAuthenticationFilter;


@Configuration
public class SpringSecurityConfig {
    
    @Autowired
    private AuthenticationConfiguration authConfiguration;

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
        return authConfiguration.getAuthenticationManager();
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
        .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }

}
