package ca.g26final.model.users;

public class Staff extends User {

    public Staff(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 5;
    }
}