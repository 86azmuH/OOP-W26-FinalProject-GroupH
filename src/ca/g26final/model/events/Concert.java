package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Concert extends Event{
    private final int ageRestriction; // display only in Phase 1

    public Concert(String eventId, String title, LocalDateTime dateTime, String location, int capacity, int ageRestriction) {
        super(eventId, title, dateTime, location, capacity);

        //Validation: doesn't allow negative ages
        if (ageRestriction < 0) {
            throw new IllegalArgumentException("ageRestriction cannot be negative");
        }
        this.ageRestriction = ageRestriction;
    }

    //Getter
    public int getAgeRestriction() {
        return ageRestriction;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", ageRestriction=" + ageRestriction + "}";
    }

}
