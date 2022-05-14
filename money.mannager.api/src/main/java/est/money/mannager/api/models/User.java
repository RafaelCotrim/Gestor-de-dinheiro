package est.money.mannager.api.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private boolean isAdmin;

    private byte[] salt;
    private byte[] hashPass;

    @OneToMany(mappedBy="user")
    private List<Transaction> transactions;

    @OneToMany(mappedBy="user")
    private List<Category> categories;

    @OneToMany(mappedBy="user")
    private List<Budget> budgets;

    public User(){}

    public User(String name, String email, byte[] salt, byte[] hashPass, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.salt = salt;
        this.hashPass = hashPass;
        this.transactions = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.budgets = new ArrayList<>();
        this.isAdmin = isAdmin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getHashPass() {
        return hashPass;
    }

    public void setHashPass(byte[] hashPass) {
        this.hashPass = hashPass;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}