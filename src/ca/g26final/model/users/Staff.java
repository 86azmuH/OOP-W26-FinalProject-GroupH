package ca.g26final.model.users;
// This declares the package this class belongs to.
// It matches your folder structure.

public class Staff extends User {


    public Staff(String userId, String name, String email) {
        // Constructor for creating a Staff object.
        // It requires userId, name, and email.

        super(userId, name, email);
        // Calls the constructor of the parent class (User).
        // This initializes the inherited fields: userId, name, and email.
    }

    @Override
    // This indicates that we are overriding a method from the User class.

    public int getMaxConfirmedBookings() {


        return 5;
        /
    }
}