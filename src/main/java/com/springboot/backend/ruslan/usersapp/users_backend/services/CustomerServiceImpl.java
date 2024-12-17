package com.springboot.backend.ruslan.usersapp.users_backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Customer;
import com.springboot.backend.ruslan.usersapp.users_backend.repositories.ICustomerRepository;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;
    

    public CustomerServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List<Customer> findALL() {
        
       return ((List<Customer>)this.customerRepository.findAll()).stream().toList();
    }
     

    @Override
    public Optional<Customer> findByLastname(String lastname) {
        return this.customerRepository.findByLastname(lastname);
    }


    @Override
    public Customer saveCust(Customer customer) {
        return this.customerRepository.save(customer);
    }

  


}
