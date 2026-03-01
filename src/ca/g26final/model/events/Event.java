package ca.g26final.model.events;
import java.time.LocalDateTime;

public class Event {

    //Declaration of Needed variables
    private String eventID;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private int capacity;
    private EventStatus status;

    //Constructor
    public Event(String eventID, String title, LocalDateTime dateTime, String location, int capacity)
    {
        //These return errors if entered blank
        if (eventID == null || eventID.isBlank()) {
            throw new IllegalArgumentException("eventId cannot be null/blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title cannot be null/blank");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("location cannot be null/blank");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }

        this.eventID = eventID;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity = capacity;
        this.status = EventStatus.ACTIVE;
    }

    // Getters
    public String getEventId() {
        return eventID;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public EventStatus getStatus() {
        return status;
    }

    //Setters
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            System.out.println("[Event] Title not updated");
            return;
        }
        this.title = title;
    }

    //setters for event service
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            System.out.println("[Event] dateTime not updated (null)");
            return;
        }
        this.dateTime = dateTime;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            System.out.println("[Event] Location not updated (invalid)");
            return;
        }
        this.location = location;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            System.out.println("[Event] Capacity not updated");
            return;
        }
        this.capacity = capacity;
    }

    //Status
    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    public boolean isActive() {
        return this.status == EventStatus.ACTIVE;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "eventId='" + eventID + '\'' +
                ", title='" + title + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }


}
