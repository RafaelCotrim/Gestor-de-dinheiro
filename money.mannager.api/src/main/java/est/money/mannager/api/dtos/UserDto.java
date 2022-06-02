package est.money.mannager.api.dtos;

import est.money.mannager.api.models.User;

public class UserDto {
    private long id;
    private String name;
    private String email;
    private boolean isAdmin;

    public UserDto(){}

    public UserDto(long id, String name, String email, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static UserDto from(User u ){
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.isAdmin());
    }
}
