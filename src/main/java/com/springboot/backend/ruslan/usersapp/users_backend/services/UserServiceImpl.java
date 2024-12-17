package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Role;
import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;
import com.springboot.backend.ruslan.usersapp.users_backend.models.UserRequest;
import com.springboot.backend.ruslan.usersapp.users_backend.repositories.RoleRepository;
import com.springboot.backend.ruslan.usersapp.users_backend.repositories.UserRepository;


/**
 ** UserServiceImpl es una clase que implementa la interfaz UserService.
 * Una vez que se implementa la interfaz se deben definir los métodos de la interfaz.
 * Dichos métodos se construyen en su interior con metodos de la clase UserRepository.
 * Dicha clase es la que se encarga de realizar las operaciones CRUD.
 * 
 * Patrón singleton: se asegura de que solo haya una instancia de la clase UserServiceImpl,
 * es decir, solo se puede acceder a una instancia de la clase UserServiceImpl a la vez.
 * Son atomicas, es decir, solo se puede acceder a una instancia de la clase UserServiceImpl a la vez.
 */

/**
 * @Service Indica que la clase es un servicio de Spring Boot que se encarga de la lógica de negocio
 * Es decir es la clase que se encarga de realizar las operaciones CRUD.
 * @Autowired Inyección de dependencias para asi poder acceder a los métodos de la clase UserRepository
 * @Transactional se utiliza para definir el alanance de una transacción en un método. Un transacción
 * es una unidad de trabajo que se ejecuta de forma atómica, es decir, o se ejecuta completamente o no se ejecuta.
 * Garantiza que las operaciones dentro de la transacción se ejecuten de manera aislada y segura para mantener la base de datos
 * segura y consistente.
 * 
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

  
    private PasswordEncoder passwordEncoder;
    

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true) //Indica que el método es de solo lectura
    @Override
    public List<User> findALL() {

        return ((List<User>) this.repository.findAll()).stream().map(user -> {
            boolean admin = user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            user.setAdmin(admin);
            return user;
            
        }).collect(Collectors.toList()); 

    }
    // La transacción de solo lectura lo que permite optimizar la base de datos, ya que no se bloquea la base de datos.
    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {

        return repository.findById(id);
      
    }
    
    @Transactional
    @Override
    public User save(User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(role -> roles.add(role));

        if (user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(role -> roles.add(role));
        }

       
        //NOTA: se me olvidó pasar el rol al usuario antes de guardar el usuario en la base de datos
        user.setRoles(roles);
        //Encripta la contraseña del usuario antes de guardarla en la base de datos
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
        
    }

    @Transactional
    @Override
    public Optional<User> update(UserRequest user, Long id) {

        Optional<User> userOptional = repository.findById(id);
        if(userOptional.isPresent()) {
            User userBD = userOptional.get();
            userBD.setEmail(user.getEmail());
            userBD.setLastname(user.getLastname());
            userBD.setName(user.getName());
            userBD.setUsername(user.getUsername());
            userBD.setBirthday(user.getBirthday());
            userBD.setGender(user.getGender());
            userBD.setCountry(user.getCountry());
            userBD.setMunicipality(user.getMunicipality());
            userBD.setProvince(user.getProvince());
            userBD.setAdmin(user.isAdmin());
          

            //Antes de guardar actualizar el rol del usuario
            List<Role> roles = new ArrayList<>();
            Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
            optionalRoleUser.ifPresent(role -> roles.add(role));

            if (user.isAdmin()){
                Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
                optionalRoleAdmin.ifPresent(role -> roles.add(role));
            }
            userBD.setRoles(roles); //Actualiza el rol del usuario
            repository.save(userBD); //Guarda el usuario actualizado en la base de datos
            return Optional.of(userBD); //Devuelve un Optional con el usuario actualizado
        }

        return Optional.empty(); //Devuelve un Optional vacío si no se encuentra el usuario
       
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
        
    }

   

}
