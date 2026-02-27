package ca.g26final.model.users;

public abstract class User {

    private String userId;
    private String name;
    private String email;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract int getMaxConfirmedBookings();

    @Override
    public String toString() {
        return userId + " - " + name + " (" + email + ")";
    }
}