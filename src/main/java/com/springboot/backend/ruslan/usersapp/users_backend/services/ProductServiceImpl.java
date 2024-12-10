package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Product;
import com.springboot.backend.ruslan.usersapp.users_backend.repositories.IProductRepository;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    

    public ProductServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    @Transactional(readOnly = true)
    @Override
    public List<Product> findALL() {
        
        return ((List<Product>)this.productRepository.findAll()).stream().toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }


    @Override
    public Product saveProd(Product product) {
        return this.productRepository.save(product);
    }



    



    

}
