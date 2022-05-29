package est.money.mannager.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double value;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference("user_budget")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    @JsonBackReference("category_budget")
    @NotNull
    private Category category;

    public Budget(){}

    public Budget(double value, User user, Category category) {
        this.value = value;
        this.user = user;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
