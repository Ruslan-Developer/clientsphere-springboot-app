package com.springboot.backend.ruslan.usersapp.users_backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

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

    @NotBlank //Indica que el campo no puede estar vacío ni ser nulo ni contener solo espacios en blanco
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 4, max = 20)
    private String username;
    @NotBlank
    private String password;
    /**
     * Al tener el usuario varios roles, se establece una relación de muchos a muchos por ello
     * creamos una lista de roles que se mapea a la tabla roles.
     */
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) //Evita la recursividad en la serialización JSON
    @ManyToMany(fetch = FetchType.LAZY)
    /**
     * Con @JoinTable indicamos que la relación entre las tablas users y roles se mapea a la tabla users_roles.
     * Con @JoiColummn conseguimos mapear la relación entre las tablas users y users_roles
     * Luego usamos inverseJoinColumns para mapear la relación inversa entre las tablas roles y users_roles
     * Al tener aqui los roles de Role que está mapeada a la tabla roles, se establece la relación entre users y roles
     * Luego tenemos que indicar los campos únicos user_id y role_id con la anotación @UniqueConstraint.
     */
    @JoinTable(name = "users_roles", 
    joinColumns = {@JoinColumn(name="user_id")},
    inverseJoinColumns = {@JoinColumn(name="role_id")},
    uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
    ) 
    private List<Role> roles;

    public User() {
        this.roles = new ArrayList<>();
    }

    
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


    public List<Role> getRoles() {
        return roles;
    }


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    

}
