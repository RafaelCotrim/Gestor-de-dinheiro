package com.example.monneymannagerapp.api.dtos;

public class UserForUpdate {
    public String name;
    public String email;
    public String password;
    public String confirmation;

    public UserForUpdate(String name, String email, String password, String confirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmation = confirmation;
    }
}
