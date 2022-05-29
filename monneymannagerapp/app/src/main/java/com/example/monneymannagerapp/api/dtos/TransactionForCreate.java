package com.example.monneymannagerapp.api.dtos;

import java.util.Date;

public class TransactionForCreate {
    public double value;
    public long userId;
    public long categoryId;
    public String date;

    public TransactionForCreate(double value, long userId, long categoryId, String date) {
        this.value = value;
        this.userId = userId;
        this.categoryId = categoryId;
        this.date = date;
    }
}
