package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;

public interface UserService {

    List<User> findALL();

    Optional<User>findByUser(Long id); //Optional es un contenedor que puede o no contener un valor no nulo

    User save(User user); //Guarda un usuario en la base de datos y devuelve el usuario guardado

    void deleteById(Long id); //Elimina un usuario de la base de datos

}
