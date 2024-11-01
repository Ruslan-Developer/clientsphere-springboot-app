package com.springboot.backend.ruslan.usersapp.users_backend.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.springboot.backend.ruslan.usersapp.users_backend.auth.TokenJWTConfig.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
      
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = null;
        String password = null;
      

        try {
            /**
             * Se crea un objeto User a partir de los datos del usuario que se envían en la petición.
             * El método readValue de ObjectMapper se utiliza para deserializar (convertir) datos JSON en un objeto Java.
             * request.getInputStream() proporciona el flujo de entrada que contiene los datos JSON.
             * User.class es la clase de destino a la que se desea convertir los datos JSON.
             */
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
         
            e.printStackTrace();
        } catch (DatabindException e) {
           
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }

       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

       
        return this.authenticationManager.authenticate(authenticationToken);
    }

    /**
     * Este metodo genera un token de autenticación si la autenticación es exitosa.
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        
        String username = user.getUsername();
        /**
         * ! NOTA: Revisar funcionamiento de esta parte del código, en concreto:
         * ! Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
         * ! Claims claims = Jwts
         * ! String jwt = Jwts.builder()
         */
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        //Si encuentra el rol de administrador, isAdmin será true.
        boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        /**
         * Creamos un objeto Claims para almacenar información adicional en el token.
         * En este caso, almacenamos los roles del usuario y el nombre de usuario.
         */
        Claims claims = Jwts
                .claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("username", username)
                .add("isAdmin", isAdmin) //Si isAdmin es true, lo agregamos al token.
                .build();

        String jwt = Jwts.builder()
            .subject(username)
            .claims(claims)  //Añade la información adicional al token de autenticación como los roles y el nombre de usuario.
            .signWith(SECRET_KEY) //Firma el token con la clave secreta.
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000)) //1 hora expiración del token.
            .compact(); //Genera el token de autenticación.

        //Pasamos en la cabecera de la respuesta el token de autenticación.
        /** El esquema "Bearer" se especifica en el encabezado Authorization de una solicitud HTTP 
         * para indicar que el cliente está proporcionando un token de acceso que debe ser utilizado 
         * para autenticar la solicitud.
         */
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwt);

        Map<String, String> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username", username);
        body.put("message", String.format("%s Autenticación exitosa", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body)); //Convierte a un String con estructura JSON.
        response.setContentType(CONTENT_TYPE); //Tipo de contenido JSON.
        response.setStatus(200); //Código de estado HTTP 200 OK.
    }
    /**
     * Este metodo se ejecuta si la autenticación falla manda el error HTTP 401 de no autorizado.
     * Estos errores se comprueban con las validaciones en el Postman.
     */

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
        Map<String, String> body = new HashMap<>();

        body.put("message", "Error de autenticación: " + failed.getMessage());
        body.put("error", failed.getMessage());
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);
    }

}
