package est.money.mannager.api.dtos;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Transaction;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BudgetDto {
    public long id;
    public double value;
    public double spentValue;
    public ContextInfo category;

    public BudgetDto(long id, double value, double spentValue, ContextInfo category) {
        this.id = id;
        this.value = value;
        this.spentValue = spentValue;
        this.category = category;
    }
    public static BudgetDto from(Budget b){
        return from(b, false, null, null);
    }
    public static BudgetDto from(Budget b, boolean includeCategoryInfo, Date dateStart, Date dateEnd){

        ContextInfo c = null;
        double sum = 0;
        if(includeCategoryInfo && b.getCategory().getTransactions().size() > 0){
            Stream<Transaction> s = b.getCategory()
                    .getTransactions()
                    .stream().filter(t -> t.getValue() < 0);

            if (dateStart == null ^ dateEnd == null){
                throw new ResponseStatusException(BAD_REQUEST);
            } else if(dateStart != null){

                if(dateStart.compareTo(dateEnd) > 0){
                    throw new ResponseStatusException(BAD_REQUEST);
                }

                s = s.filter(t -> t.getDate().compareTo(dateStart) >= 0 && t.getDate().compareTo(dateEnd) <= 0);
            }

            sum = s.map(Transaction::getValue).reduce(0.0, Double::sum);

            c = new ContextInfo(b.getCategory().getId(), b.getCategory().getName());
        }

        return new BudgetDto(b.getId(), b.getValue(), Math.abs(sum),  c);
    }
}
