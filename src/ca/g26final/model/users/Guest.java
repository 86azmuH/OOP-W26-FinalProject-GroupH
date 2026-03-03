package ca.g26final.model.users;
/*
Guest Users
- Most restrcited booking limit in the system
- Can only have max of 1 confirmed booking at a time.
 */
public class Guest extends User {



    public Guest(String userId, String name, String email) {
        // Calls the constructor of the parent class (user)
        super(userId, name, email);// Calling constructor of abstract user class
    }

    @Override // Tells java to override a method from the parent class
    public int getMaxConfirmedBookings() {
        return 1;
    }
}