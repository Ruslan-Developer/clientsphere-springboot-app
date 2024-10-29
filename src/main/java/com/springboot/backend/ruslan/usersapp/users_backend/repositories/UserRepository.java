package com.springboot.backend.ruslan.usersapp.users_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;
/**
 * UserRepository es una interfaz que extiende de CrudRepository,
 * que es una interfaz de Spring Data JPA que nos proporciona métodos para realizar operaciones CRUD
 * para manejar la entidad User, viene con métodos como save, findById, findAll, deleteById, etc...
 * ya implementados por Spring Data JPA.
 * <> Hay que indicar el tipo de la entidad (Entity) y el tipo de la clave primaria de la entidad.
 */
 
public interface UserRepository extends CrudRepository<User, Long> {

}
