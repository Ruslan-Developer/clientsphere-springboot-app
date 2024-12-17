package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;
import com.springboot.backend.ruslan.usersapp.users_backend.repositories.UserRepository;

@Service
public class JPAUserDetailsService implements UserDetailsService   {

    @Autowired
    private UserRepository repository;
    /**
     * El método loadUserByUsername se implementa de la interfaz UserDetailsService.
     * Esta interfaz es utilizada por el framework de Spring Security 
     * para obtener los detalles del usuario durante el proceso de autenticación.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        /**
         * Se llama al método findByUsername de la interfaz UserRepository para buscar un usuario por su nombre de usuario.
         * Se guarda en una variable de tipo Optional<User> que evita el uso del null y maneja la posibilidad de que el usuario no exista.
         * Si el usuario no existe, se lanza una excepción UsernameNotFoundException.
         * Si el usuario existe, se asigna a la variable user.
         */
        Optional<User> optionalUser = repository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(String.format("Usuario %s no existe en el sistema", username));
        }

        User user = optionalUser.orElseThrow();
        /**
         * Se crea una lista de GrantedAuthority a partir de la lista de roles del usuario encontrado
         * y se asigna a la variable authorities del tipo List<GrantedAuthority>.
         * Se usa el método stream() para convertir la lista de roles en un flujo de datos.
         * Se usa el método map() para convertir cada rol de objeto JSON en un SimpleGrantedAuthority.
         * Se usa el método collect() para recopilar los SimpleGrantedAuthority en una lista.
         */
        List<GrantedAuthority> authorities = user.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
        /**
         * Se crea un objeto especial de Spring Security User con el nombre de usuario, la contraseña, los roles y otros atributos.
         * Se devuelve el objeto UserDetails.
         * Se le pasa el nombre de usuario, la contraseña, si la cuenta está habilitada, si la cuenta no ha expirado, 
         * si las credenciales no han expirado, si la cuenta no está bloqueada, si tiene los roles asignados.
         * Si la conotraseña es correcta genera un token de autenticación.
         */
       
        return new org.springframework.security.core.userdetails.User(username, 
        user.getPassword(), //Valida la contraseña del usuario con la contraseña almacenada en la base de datos.
        true, 
        true, 
        true, 
        true, 
        authorities);
    }

}
