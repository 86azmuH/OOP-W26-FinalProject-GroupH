package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Concert extends Event{
    private final String ageRestriction; // display only in Phase 1

    public Concert(String eventID, String title, LocalDateTime dateTime, String location, int capacity, String ageRestriction) {
        super(eventID, title, dateTime, location, capacity);

        if (ageRestriction == null || ageRestriction.isBlank()) {
            System.out.println("[Concert] Invalid ageRestriction. Using 'N/A'.");
            this.ageRestriction = "N/A";
        } else {
            this.ageRestriction = ageRestriction;
        }
    }

    //Getter
    public String getAgeRestriction() {
        return ageRestriction;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", ageRestriction=" + ageRestriction + "}";
    }

}
