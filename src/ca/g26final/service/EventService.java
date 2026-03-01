package ca.g26final.service;

import ca.g26final.model.events.Event;
import ca.g26final.model.events.EventStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventService {

    //Stores all events in an array
    private ArrayList<Event> events;

    public EventService() {
        events = new ArrayList<>();
    }

    //Adds an event if valid and eventID is unique
    public boolean addEvent(Event event) {
        if (event == null) {
            System.out.println("[EventService] addEvent failed: event is null");
            return false;
        }

        if (event.getEventId() == null || event.getEventId().isBlank()) {
            System.out.println("[EventService] addEvent failed: eventId is blank");
            return false;
        }

        if (getEventById(event.getEventId()) != null) {
            System.out.println("[EventService] addEvent failed: duplicate eventId " + event.getEventId());
            return false;
        }

        events.add(event);
        return true;
    }

    public Event getEventById(String eventId) {
        if (eventId == null || eventId.isBlank()) return null;

        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    //returns all events
    public ArrayList<Event> getAllEvents(){
        return events;
    }

    //Updates event fields
    public boolean updateEvent(String eventId, String newTitle, LocalDateTime newDateTime, String newLocation, int newCapacity) {
        Event e = getEventById(eventId);
        if (e == null) {
            System.out.println("[EventService] updateEvent failed: event not found " + eventId);
            return false;
        }

        if (newTitle == null || newTitle.isBlank()) {
            System.out.println("[EventService] updateEvent: title not updated (blank)");
        } else {
            e.setTitle(newTitle);
        }

        if (newDateTime == null) {
            System.out.println("[EventService] updateEvent: dateTime not updated (null)");
        } else {
            e.setDateTime(newDateTime);
        }

        if (newLocation == null || newLocation.isBlank()) {
            System.out.println("[EventService] updateEvent: location not updated (blank)");
        } else {
            e.setLocation(newLocation);
        }

        if (newCapacity <= 0) {
            System.out.println("[EventService] updateEvent: capacity not updated (must be > 0)");
        } else {
            e.setCapacity(newCapacity);
        }

        return true;
    }
    //Cancels an event
    public boolean cancelEvent(String eventId) {
        Event e = getEventById(eventId);
        if (e == null) {
            System.out.println("[EventService] cancelEvent failed: event not found " + eventId);
            return false;
        }

        e.cancel();
        return true;
    }

    //true for when an event exists and is active
    public boolean isEventActive(String eventId) {
        Event e = getEventById(eventId);
        return e != null && e.getStatus() == EventStatus.ACTIVE;
    }
}
