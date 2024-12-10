package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Product;

public interface IProductService {

        List<Product>  findALL();

        Optional<Product>findById(Long id);

        Product saveProd(Product product);

}
