package com.example.monneymannagerapp.api.dtos;

import java.util.Date;

public class TransactionDto {
    public long id;
    public double value;
    public Date date;
    public ContextInfo user;
    public ContextInfo category;

    public static class ContextInfo{
        public long id;
        public String name;
    }
}
