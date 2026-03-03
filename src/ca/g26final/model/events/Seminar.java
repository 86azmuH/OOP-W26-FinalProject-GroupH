package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Seminar extends Event{
    private final String speakerName;

    //Constructor
    public Seminar(String eventID, String title, LocalDateTime dateTime, String location, int capacity, String speakerName) {
        //Calls Super Class Constructor
        super(eventID, title, dateTime, location, capacity);

        //Checks if blank
        if (speakerName == null || speakerName.isBlank()) {
            System.out.println("[Seminar] Invalid speakerName. Using 'Unknown Speaker'.");
            this.speakerName = "Unknown Speaker";
        } else {
            this.speakerName = speakerName;
        }
    }

    //returns Slideshow
    public String getSpeakerName() {
        return speakerName;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", speakerName='" + speakerName + "'}";
    }
}
