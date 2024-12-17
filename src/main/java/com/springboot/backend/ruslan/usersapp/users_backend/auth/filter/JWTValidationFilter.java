package com.springboot.backend.ruslan.usersapp.users_backend.auth.filter;

import static com.springboot.backend.ruslan.usersapp.users_backend.auth.TokenJWTConfig.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.ruslan.usersapp.users_backend.auth.SimpleGrantedAuthorityJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Es un filtro de autenticación que se utiliza para validar tokens JWT en las peticiones.
 * Extiende de la clase BasicAuthenticationFilter que es un filtro de autenticación básico de Spring Security.
 * Se encarga de validar el token JWT que se envía en la cabecera de la petición.
 * Si el token es válido, se extraen los claims (reclamaciones) del token, incluyendo el nombre de usuario (username)
 *  y los roles (authorities).
 * Se crean los roles a partir de las authorities obtenidas del token.
 * Se crea un objeto UsernamePasswordAuthenticationToken con el nombre de usuario y los roles.
 */

public class JWTValidationFilter extends BasicAuthenticationFilter{


    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

           
        
            String header = request.getHeader(HEADER_AUTHORIZATION);

            if(header == null  || !header.startsWith(PREFIX_TOKEN)){

                chain.doFilter(request, response); // Si no hay token o no empieza con el prefijo Bearer, se pasa al siguiente filtro
                return;
            }

            String token = header.replace(PREFIX_TOKEN, "");
            
            try{
                Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
                String username = claims.getSubject();
                Object authoritiesClaims = claims.get("authorities"); // Se obtienen los roles del token

                Collection<? extends GrantedAuthority> roles = Arrays.asList(new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)); 
                
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, 
                roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            }catch(JwtException e){
                
                Map<String, String> body = new HashMap<>();
                body.put("error", e.getMessage());
                body.put("message", "El token no es valido");

                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setStatus(401);
                response.setContentType(CONTENT_TYPE);
            }
            
       
    }

    

}
