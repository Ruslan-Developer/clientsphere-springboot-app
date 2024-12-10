package com.springboot.backend.ruslan.usersapp.users_backend.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String observation;

    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id") //Relacion llave foranea con la tabla cliente
    private Customer customer;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id") //Relacion llave foranea con la tabla facturas_items
    private List<ItemInvoice> items;

    
    @PrePersist //Indica que el método se ejecutará antes de que se cree la entidad en la base de datos
    public void prePersist() {
        dateCreate = new Date(); //Se asigna la fecha actual a la fecha de creación de la factura 
    }

    
    public Invoice() {
        //Se inicializa la lista de items
        this.items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String descripcion) {
        this.description = descripcion;
    }
    public String getObservation() {
        return observation;
    }
    public void setObservation(String observacion) {
        this.observation = observacion;
    }
    public Date getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ItemInvoice> getItems() {
        return items;
    }

    public void setItems(List<ItemInvoice> items) {
        this.items = items;
    }

    public Double getTotal(){
        Double total = 0.0;
        for(ItemInvoice item : items) {
            total += item.calcularImporte();
        }
        return total;
    }


}
