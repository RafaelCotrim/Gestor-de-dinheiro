package est.money.mannager.api.dtos;

import est.money.mannager.api.models.Transaction;

public class TransactionDto {

    public long id;
    public double value;
    public ContextInfo user;
    public ContextInfo category;

    public TransactionDto(long id, double value) {
        this.id = id;
        this.value = value;
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
                t.getValue()
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
