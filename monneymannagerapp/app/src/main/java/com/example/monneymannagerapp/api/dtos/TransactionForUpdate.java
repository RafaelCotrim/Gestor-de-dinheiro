package com.example.monneymannagerapp.api.dtos;

import java.util.Date;

public class TransactionForUpdate {
    public double value;
    public String date;
    public long categoryId;
    public long userId;

    public TransactionForUpdate(double value, String date, long categoryId, long userId) {
        this.value = value;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
