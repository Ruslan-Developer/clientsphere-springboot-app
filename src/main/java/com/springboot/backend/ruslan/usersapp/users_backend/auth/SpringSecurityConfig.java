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

    /**
     * Método que nos permite configurar la seguridad de la aplicación.
     * Se configura el filtro de seguridad, se deshabilita el CSRF, se configura la política de sesiones y se añaden los filtros de autenticación y validación del token JWT.
     * Con todo esto controlamos el acceso a los recursos de la aplicación, mediante la configuración de los permisos de acceso a los recursos.
     * Mediante los roles de los usuarios establecemos los permisos de acceso a los distintos recursos de la aplicación.
     * @param http
     * @return
     * @throws Exception
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(authz ->
        authz
        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/{users}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/customers", "/api/customers/{customers}").permitAll()
        
        //.requestMatchers(HttpMethod.GET, "/api/customers/{lastname}").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{products}").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("USER", "ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
        //Permitir acceso a la docuiemntación de springdoc-openapi
        .requestMatchers("/v3/api-docs/**").permitAll()
        .requestMatchers("/swagger-ui/**").permitAll()
        .requestMatchers("/swagger-ui.html").permitAll()

        .anyRequest().authenticated())
        .cors(cors -> cors.configurationSource(configurationSource()))
        .addFilter(new JWTAuthenticationFilter(authenticationManager())) // Se añade el filtro de autenticación JWT
        .addFilter(new JWTValidationFilter(authenticationManager())) // Se añade el filtro de validación del token JWT
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();

    }

    /**
     * Método que nos permite configurar la política de CORS de la aplicación es decir solicitudes de recursos de origen cruzado.
     * Es útil para cuando el cliente y el servidor se encuentran en diferentes dominios. 
     * Aqui se permite el acceso desde localhost:4200 que es donde se encuentra el cliente de nuestra aplicación en Angular.
     * Configuramos los metodos HTTP permitidos, los headers permitidos y si se permiten las credenciales.
     * @return devuelve un objeto con la confuiguracion CORS que tiene como finalidad permitir el acceso a los recursos de la aplicación desde un origen distinto al del servidor.
     */
    
    @Bean 
    CorsConfigurationSource configurationSource(){

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); //Se permite que podamos establecer en el controlador el acceso desde cualquier origen.
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); //Se permite el acceso desde solo desde el origen http://localhost:4200.
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); 
        config.setAllowCredentials(null); //No se permite el envío de credenciales como cookies o autenticación HTTP básica.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); //Se registra la configuración CORS para todas las rutas de la aplicación.
        return source;

     }
     /**
      * Método que nos permite registrar un filtro de CORS en la aplicación.
      * Se configura el filtro de CORS para que se ejecute antes que cualquier otro filtro.
      * Se le pasa nuestra configuración CORS para que sepa como gestionar las solicitudes de recursos de origen cruzado.
      * @return Finalmente, el filtro corsBean (que ahora está registrado con la configuración CORS y tiene la más alta prioridad) 
      * es devuelto para que Spring Boot lo gestione y lo aplique durante el procesamiento de solicitudes HTTP
      */

     @Bean
     FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(
            new CorsFilter(this.configurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return corsBean;
     }

}
