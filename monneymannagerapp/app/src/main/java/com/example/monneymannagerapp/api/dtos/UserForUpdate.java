package com.example.monneymannagerapp.api.dtos;

public class UserForUpdate {
    public String name;
    public String email;
    public String password;
    public String confirmation;
    public boolean admin;

    public UserForUpdate(String name, String email, String password, String confirmation, boolean admin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmation = confirmation;
        this.admin = admin;
    }
}
