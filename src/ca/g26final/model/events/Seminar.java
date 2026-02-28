package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Seminar extends Event{
    private final String speakerName;

    public Seminar(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String speakerName) {
        super(eventId, title, dateTime, location, capacity);

        if (speakerName == null || speakerName.isBlank()) {
            throw new IllegalArgumentException("speakerName cannot be null/blank");
        }//Error if Blank
        this.speakerName = speakerName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", speakerName='" + speakerName + "'}";
    }
}
