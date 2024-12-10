package com.springboot.backend.ruslan.usersapp.users_backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * UserRequest es una clase que se utiliza para recibir los datos de un usuario
 * que no contiene la contraseña del usuario. 
 * De ese modo al actualizar nunca se cambia la contraseña a través del formulario.
 * 
 */

public class UserRequest {

    @NotEmpty
    private String lastname;

    @NotBlank 
    private String name;

    @NotBlank
    private String birthday;

    @NotBlank
    private String gender;

    @NotBlank
    private String country;

    @NotBlank
    private String municipality;

    @NotBlank
    private String province;

    @NotNull
    @Min(100000000)
    @Max(999999999)
    private Integer phone;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 4, max = 20)
    private String username;

    private boolean admin;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
}
