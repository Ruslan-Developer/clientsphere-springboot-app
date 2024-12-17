package com.springboot.backend.ruslan.usersapp.users_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.Product;
import com.springboot.backend.ruslan.usersapp.users_backend.services.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(originPatterns = {"*"}) // Permite el acceso a la API desde cualquier origen usando originPatterns 
@Tag(name = "Productos", description = "API para gestionar los productos")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Operation(summary = "Obtener todos los productos", description = "Obtiene una listado de todos los productos disponibles en la BBDD")
       @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados"),
            @ApiResponse(responseCode = "404", description = "Productos no encontrados")
    })
    @GetMapping
    public List<Product> list() {
        return productService.findALL();
    }

    @Operation(summary = "Obtener un producto por su ID", description = "Método personalizado que obtiene un producto por su ID de la BBDD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado por el ID proporcionado")
    })
    @GetMapping("{id}")
    public Product showProduct(
        @Parameter(description = "Introduzca: ID del producto a buscar en la BBDD") @PathVariable Long id) {
        return productService.findById(id).get();
    }

    @Operation(summary = "Crear un producto", description = "Método personalizado que crea un producto en la BBDD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error en la validación de los datos")
    })

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult result) {

        if (result.hasErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProd(product));

    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
