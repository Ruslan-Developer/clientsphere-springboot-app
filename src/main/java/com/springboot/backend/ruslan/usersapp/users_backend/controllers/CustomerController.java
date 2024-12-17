package com.springboot.backend.ruslan.usersapp.users_backend.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Customer;
import com.springboot.backend.ruslan.usersapp.users_backend.services.ICustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin(originPatterns = {"*"}) // Permite el acceso a la API desde cualquier origen
//@CrossOrigin(origins = "http://localhost:4201") // Permite el acceso a la API desde un puerto concreto
@RestController
@Tag(name = "Clientes", description = "API para gestionar los clientes")
@RequestMapping("/api/customers")
public class CustomerController {

    
    @Autowired
    private ICustomerService customerService;
    @Operation(summary = "Obtener todos los clientes", description = "Obtiene una listado de todos los clientes disponibles en la BBDD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
        @ApiResponse(responseCode = "404", description = "Clientes no encontrados")
    })
    @GetMapping
    public List<Customer> getAllClientes(){
        return customerService.findALL();
    }
     
    @Operation(summary = "Obtener un cliente por su apellido", description = "Método personalizado que obtiene un cliente por su apellido de la BBDD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado por el apellido proporcionado")
    })
    @GetMapping("/{lastname}")

    public ResponseEntity<?> showCustomerByLastname(
        @Parameter(description = "Introduzca un apellido para encontrar un cliente en la BBDD") @PathVariable String lastname){

         Optional<Customer> customer =  customerService.findByLastname(lastname);
         
        if(customer.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(customer.orElseThrow());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "el cliente con ese apellido no existe"));
        }
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Método para crear un nuevo cliente en la BBDD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta")
    })
    //@SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody @Parameter(description = "Campos obligatorios * del cliente a rellenar para crear uno nuevo") Customer customer){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCust(customer));
    }
    


}
