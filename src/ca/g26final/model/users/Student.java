package ca.g26final.model.users;
// Declares the package location of this class.

public class Student extends User {

// This means Student inherits all common fields and methods from User.

    public Student(String userId, String name, String email) {
        // Constructor used to create a new Student object.
        // It requires userId, name, and email.

        super(userId, name, email);
        // Calls the constructor of the parent class (User).
        // This initializes the inherited fields: userId, name, and email.
    }

    @Override
    // Indicates this method overrides an abstract method from User.

    public int getMaxConfirmedBookings() {
        // Defines the maximum number of CONFIRMED bookings allowed for a Student.

        return 3;

    }
}