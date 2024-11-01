package com.springboot.backend.ruslan.usersapp.users_backend.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.ruslan.usersapp.users_backend.entities.User;
import com.springboot.backend.ruslan.usersapp.users_backend.models.UserRequest;
import com.springboot.backend.ruslan.usersapp.users_backend.services.UserService;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



/**
 ** @RestController que define nuestra clase como un controlador RESTful:
 * es decir, capaz de manejar solicitudes HTTP y devolver respuestas en formato JSON,
 * pero también puede devolver XML, CSV o cualquier otro formato según sea necesario.
 * 
 * @CrossOrigin es un mecanismo que permite que los recursos en una página web 
 * sean solicitados desde otro dominio distinto al dominio desde el cual se sirvió la página.
 * Habilita las solicitudes HTTP desde otros dominios, lo cual es útil cuando tu frontend y backend 
 * están en diferentes dominios o puertos.
 * 
 */
@CrossOrigin(origins = {"http://localhost:4200"}) 
@RestController 
@RequestMapping("/api/users") //Define la URL base para acceder a los métodos de la clase UserController
public class UserController {
    @Autowired 
    private UserService service;

    /**
     * La anotación @GetMapping sirve para realizar una solicitud HTTP para recuperar 
     * en este caso una lista de usuarios del servidor.
     * @return devuelve una lista de usuarios y el estado HTTP 200 OK.
     */

    @GetMapping 
    public List<User> list(){
        return service.findALL();
    }

    /**
     * El método show() se encarga de mostrar un usuario en concreto.
     * La anotación @GetMapping indica que el método maneja peticiones GET.
     * @param id pasa el id del usuario que se quiere mostrar.
     * @return devuelve un objeto de tipo ResponseEntity<?> con el usuario y el estado HTTP 200 OK.
     */

    @GetMapping("/{id}") //Es un PathVariable lo que nos permite obtener el id de la URL
    public ResponseEntity<?> show(@PathVariable Long id) {
     
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow()); //Tambien podemos usar el método get() para obtener el valor
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "el usuario no esta registrado con el id: " + id));
        }

    }
    /**
     * La anotación @PostMapping indica un tipo de solicitud de HTTP utilizada para enviar datos
     * al servidor para crear un recurso nuevo sino tiene un id asignado.
     * @param user le pasamos un objeto de tipo User que se recibe en el cuerpo de la petición.
     * @return devuelve un objeto de tipo ResponseEntity<User> con el usuario guardado y el estado HTTP 201 Created.
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        /**
         * Si el objeto user tiene errores de validación, se recogen los errores en un Map y se devuelven al cliente Angular.
         */
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    /**
     * La anotación @PutMapping sirve para actualizar un recurso existente en el servidor al que se le pasa un id.
     * @param id se pasa el id del usuario que se quiere actualizar a través de la URL desde el cliente Angular.
     * @param user le pasamos un objeto JSON que se envía en el cuerpo de la petición desde el cliente Angular. 
     * @return devuelve un objeto de tipo ResponseEntity<User> con el usuario actualizado y el estado HTTP 201 Created.
     * @ResponeEntity es una clase que representa toda la respuesta HTTP: código de estado, encabezados y cuerpo al cliente.
     * Dentro del método llamamos al método update() de la clase UserServiceImpl a traves de la interfaz UserService.
     * El método update() recibe un objeto de tipo User y un id de tipo Long y devuelve un objeto del tipo Optional<User>.
     * Mediante el método isPresent() comprobamos si el objeto Optional<User> contiene un valor. 
     */
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updtate(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {

        if(result.hasErrors()){
            return validation(result);
        }

        
        Optional<User> userOptional = service.update(user, id);
        if(userOptional.isPresent()) {
     
            return ResponseEntity.ok(userOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

   

    /**
     * La anotación @DeleteMapping sirve para eliminar un recurso existente en el servidor.
     * @param id pasa el id del usuario que se quiere eliminar.
     * @return  devuelve un objeto de tipo ResponseEntity<?> con el estado HTTP 204 No Content.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if(userOptional.isPresent()) {
            service.deleteById(id);
            // Indica que la petición se ha completado con éxito y que no devuelve contenido en el cuerpo de la respuesta
            return ResponseEntity.noContent().build(); 
        }
            return ResponseEntity.notFound().build();
    }

    /**
     * Método que recoge los errores de validación.
     * @param result se le pasa un objeto de tipo BindingResult que contiene los errores de validación.
     * @return devuelve un objeto de tipo ResponseEntity<?> con los errores de validación y el estado HTTP 400 Bad Request.
     */

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
    

}
