package com.example.monneymannagerapp.api.dtos;

public class BudgetForUpdate {
    public double value;
    public long categoryId;
    public long userId;

    public BudgetForUpdate(double value, long categoryId, long userId) {
        this.value = value;
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
