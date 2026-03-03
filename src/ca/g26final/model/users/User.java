package ca.g26final.model.users;
// Declares the package this class belongs to.

public abstract class User {
// Abstract class means:
// 1. You cannot create a User object directly.
// 2. It is meant to be extended by subclasses (Student, Staff, Guest).

    private String userId;
    private String name;
    private String email;

    public User(String userId, String name, String email) {
        // Constructor used by subclasses to initialize common fields.
        this.userId = userId;
        this.name = name;
        this.email = email;

    }
//Encapsulation
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
    // Abstract method.
    // This forces every subclass (Student, Staff, Guest)
    // to provide its own booking limit implementation.
    // This is where polymorphism happens.

    @Override
    public String toString() {
        // Overrides the default Object toString() method.
        // Defines how a User object will be printed.

        return userId + " - " + name + " (" + email + ")";
        // Formats the user information into a readable string.
    }
}