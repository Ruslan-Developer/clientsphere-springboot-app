package com.springboot.backend.ruslan.usersapp.users_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Customer;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {

    //Generar un método que busque por el apellido del cliente
     Optional<Customer> findByLastname(String lastname );
   


}
