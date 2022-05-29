package com.example.monneymannagerapp.api.dtos;

public class TransactionForCreate {
    public double value;
    public long userId;
    public long categoryId;

    public TransactionForCreate(double value, long userId, long categoryId) {
        this.value = value;
        this.userId = userId;
        this.categoryId = categoryId;
    }
}
