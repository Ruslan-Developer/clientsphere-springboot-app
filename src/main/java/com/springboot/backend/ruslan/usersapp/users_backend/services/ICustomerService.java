package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Customer;

public interface ICustomerService {

    List<Customer> findALL();
    
    Optional<Customer> findByLastname(String lastname);

}
