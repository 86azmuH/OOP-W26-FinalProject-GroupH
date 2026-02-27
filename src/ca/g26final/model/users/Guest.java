package ca.g26final.model.users;

public class Guest extends User {

    public Guest(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 1;
    }
}