package ca.g26final.model.users;

public class Student extends User {

    public Student(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public int getMaxConfirmedBookings() {
        return 3;
    }
}