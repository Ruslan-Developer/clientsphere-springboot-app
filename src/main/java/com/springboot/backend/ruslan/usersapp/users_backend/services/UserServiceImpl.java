package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;
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
 * Garantiza que las operaciones dentro de la transacción se ejevuten de manera aislada y segura para mantener la base de datos
 * segura y consistente.
 * 
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;
    

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true) //Indica que el método es de solo lectura
    @Override
    public List<User> findALL() {

        return (List<User>) repository.findAll(); //Devuelve un Iterable, por lo que se hace un casting a List<User>

    }
    // La transacción de solo lectura lo que permite optimizar la base de datos, ya que no se bloquea la base de datos.
    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUser(Long id) {

        return repository.findById(id);
      
    }
    @Transactional
    @Override
    public User save(User user) {

        return repository.save(user);
        
    }

    @Transactional
    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
        
    }

}
