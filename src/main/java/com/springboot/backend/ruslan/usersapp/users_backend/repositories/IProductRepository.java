package com.springboot.backend.ruslan.usersapp.users_backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Product;

public interface IProductRepository extends CrudRepository<Product, Long> {



}
