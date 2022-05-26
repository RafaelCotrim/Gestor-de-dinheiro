package com.example.monneymannagerapp.api.dtos;

public class TransactionDto {
    public long id;
    public double value;
    public ContextInfo user;
    public ContextInfo category;

    public static class ContextInfo{
        public long id;
        public String name;
    }
}
