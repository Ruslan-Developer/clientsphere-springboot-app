package com.springboot.backend.ruslan.usersapp.users_backend.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastname;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String province;
    
    @Column(name = "postal_code")
    private Integer postalCode;

    private String country;
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) 
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    public Customer() {
        this.invoices = new ArrayList<>();
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

    public String getLastName() {
        return lastname
;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getProvince(){
        return province;
    }

    public void setProvince(String province){
        this.province = province;
    }

    public Integer getPostalCode(){
        return postalCode;
    }

    public void setPostalCode(Integer postalCode){
        this.postalCode = postalCode;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }



  



}
