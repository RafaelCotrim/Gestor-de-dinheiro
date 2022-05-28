package est.money.mannager.api.dtos;

import est.money.mannager.api.models.Transaction;

import java.util.Date;

public class TransactionDto {

    public long id;
    public double value;
    public Date date;
    public ContextInfo user;
    public ContextInfo category;

    public TransactionDto(long id, double value, Date date) {
        this.id = id;
        this.value = value;
        this.date = date;
    }

    public static class ContextInfo{
        public long id;
        public String name;

        public ContextInfo(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static TransactionDto from(Transaction t){
        return new TransactionDto(
                t.getId(),
                t.getValue(),
                t.getDate()
        );
    }

    public static TransactionDto from(Transaction t, boolean includeUserInfo, boolean includeCategoryInfo){
        TransactionDto tdto = TransactionDto.from(t);
        if(includeUserInfo && t.getUser() != null){
            tdto.user = new TransactionDto.ContextInfo(t.getUser().getId(), t.getUser().getName());
        }

        if(includeCategoryInfo && t.getCategory() != null){
            tdto.category = new TransactionDto.ContextInfo(t.getCategory().getId(), t.getCategory().getName());
        }
        return tdto;
    }
}
