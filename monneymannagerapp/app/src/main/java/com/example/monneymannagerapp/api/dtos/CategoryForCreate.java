package com.example.monneymannagerapp.api.dtos;

public class CategoryForCreate {
    public String name;
    public long userId;

    public CategoryForCreate(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }
}
