package com.springboot.backend.ruslan.usersapp.users_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Aqui se define la entidad User, que se mapea a la tabla users en la base de datos.
 * La clase User tiene los atributos id, name, lastname, email, username y password.
 * La clase User tiene los métodos getter y setter para cada atributo. 
 */

@Entity //Indica que la clase es una entidad de la base de datos
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que el valor de la clave primaria se generará automáticamente
    private Long id;

    private String name;
    private String lastname;
    private String email;
    private String username;
    private String password;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    

}
