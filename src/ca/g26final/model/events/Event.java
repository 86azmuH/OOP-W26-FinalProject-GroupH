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
    public Event(String eventID, String title, LocalDateTime dateTime, String location, int capacity) {

        boolean valid = true;

        if (eventID == null || eventID.isBlank()) {
            System.out.println("[Event] Invalid eventID. Using 'INVALID'.");
            this.eventID = "INVALID";
            valid = false;
        } else {
            this.eventID = eventID;
        }

        if (title == null || title.isBlank()) {
            System.out.println("[Event] Invalid title. Using 'Untitled Event'.");
            this.title = "Untitled Event";
            valid = false;
        } else {
            this.title = title;
        }

        if (dateTime == null) {
            System.out.println("[Event] Invalid dateTime. Using now().");
            this.dateTime = LocalDateTime.now();
            valid = false;
        } else {
            this.dateTime = dateTime;
        }

        if (location == null || location.isBlank()) {
            System.out.println("[Event] Invalid location. Using 'TBD'.");
            this.location = "TBD";
            valid = false;
        } else {
            this.location = location;
        }

        if (capacity <= 0) {
            System.out.println("[Event] Invalid capacity. Using 1.");
            this.capacity = 1;
            valid = false;
        } else {
            this.capacity = capacity;
        }

        this.status = valid ? EventStatus.ACTIVE : EventStatus.CANCELLED;
    }


    //returns Event ID
    public String getEventId() {
        return eventID;
    }

    //returns Title
    public String getTitle() {
        return title;
    }

    //returns Date Time
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    //returns Location
    public String getLocation() {
        return location;
    }

    //returns Capacity
    public int getCapacity() {
        return capacity;
    }

    //returns Status
    public EventStatus getStatus() {
        return status;
    }


    //returns Event Type
    public String getEventType() {
        return getClass().getSimpleName();
    }

    //Sets Event Title
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            System.out.println("[Event] Title not updated");
            return;
        }
        this.title = title;
    }

    //Sets Date Time
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            System.out.println("[Event] dateTime not updated (null)");
            return;
        }
        this.dateTime = dateTime;
    }

    //Sets Location
    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            System.out.println("[Event] Location not updated (invalid)");
            return;
        }
        this.location = location;
    }

    //Sets Capacity
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            System.out.println("[Event] Capacity not updated");
            return;
        }
        this.capacity = capacity;
    }

    //Status Changer to Cancel
    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    //Status Change to Active
    public boolean isActive() {
        return this.status == EventStatus.ACTIVE;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " { " +
                "eventId: " + eventID +
                "| title: " + title +
                "| dateTime: " + dateTime +
                "| location: " + location +
                "| capacity: " + capacity +
                "| status: " + status +
                " }";
    }


}
