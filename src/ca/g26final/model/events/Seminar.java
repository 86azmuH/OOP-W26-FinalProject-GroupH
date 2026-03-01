package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Seminar extends Event{
    private final String speakerName;

    public Seminar(String eventID, String title, LocalDateTime dateTime, String location, int capacity, String speakerName) {
        super(eventID, title, dateTime, location, capacity);

        if (speakerName == null || speakerName.isBlank()) {
            System.out.println("[Seminar] Invalid speakerName. Using 'Unknown Speaker'.");
            this.speakerName = "Unknown Speaker";
        } else {
            this.speakerName = speakerName;
        }
    }

    public String getSpeakerName() {
        return speakerName;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", speakerName='" + speakerName + "'}";
    }
}
