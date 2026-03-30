package ca.g26final.service;

import ca.g26final.model.events.Concert;
import ca.g26final.model.events.Event;
import ca.g26final.model.events.EventStatus;
import ca.g26final.model.events.Seminar;
import ca.g26final.model.events.Workshop;
import ca.g26final.persistence.CsvUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    //Stores all events in an array
    private ArrayList<Event> events;
    private final Path eventsCsvPath;

    public EventService() {
        this(CsvUtil.resolveDataPath("events.csv"));
    }

    public EventService(Path csvPath) {
        events = new ArrayList<>();
        this.eventsCsvPath = csvPath;
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
        try { updateFile(); } catch (Exception ignored) {}
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

        try { updateFile(); } catch (Exception ignored) {}
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
        try { updateFile(); } catch (Exception ignored) {}
        return true;
    }

    //true for when an event exists and is active
    public boolean isEventActive(String eventId) {
        Event e = getEventById(eventId);
        return e != null && e.getStatus() == EventStatus.ACTIVE;
    }

    //Cancel event by EventId
    public boolean removeEvent(String eventId){
        if(eventId == null || eventId.isBlank()){
            System.out.println("[EventService] cancelEvent failed: eventId is blank.");
            return false;
        }

        for (Event event: events){
            if(event.getEventId().equalsIgnoreCase(eventId)){
                if(event.getStatus() == EventStatus.CANCELLED){
                    System.out.println("[EventService] cancelEvent failed: event already cancelled");
                    return false;
                }
                event.cancel();
                try { updateFile(); } catch (Exception ignored) {}
                return true;
            }
        }

        System.out.println("[EventService] cancelEvent failed: event not found");
        return false;
    }

    // Persistence
    // Preferred CSV format:
    // eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction
    // Legacy format supported:
    // eventId,type,title,dateTime,location,capacity,status[,extra]
    public void loadFromCsv() throws Exception {
        List<String> lines = CsvUtil.readAll(eventsCsvPath);
        events.clear();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 7) continue;

            // Skip header rows
            if (parts[0].trim().equalsIgnoreCase("eventId")) {
                continue;
            }

            String id = parts[0].trim();
            String type;
            String title;
            String dateStr;
            String location;
            int capacity;
            String statusStr;
            String topic = "";
            String speakerName = "";
            String ageRestriction = "";

            // Preferred schema detection: event type at index 6
            if (parts.length >= 10 && isKnownEventType(parts[6].trim())) {
                title = parts[1].trim();
                dateStr = parts[2].trim();
                location = parts[3].trim();
                capacity = parseIntSafe(parts[4].trim(), 1);
                statusStr = parts[5].trim().toUpperCase();
                type = parts[6].trim().toUpperCase();
                topic = parts[7].trim();
                speakerName = parts[8].trim();
                ageRestriction = parts[9].trim();
            } else {
                // Legacy fallback schema
                type = parts[1].trim().toUpperCase();
                title = parts[2].trim();
                dateStr = parts[3].trim();
                location = parts[4].trim();
                capacity = parseIntSafe(parts[5].trim(), 1);
                statusStr = parts[6].trim().toUpperCase();
                String extra = parts.length > 7 ? parts[7].trim() : "";

                if ("WORKSHOP".equals(type)) topic = extra;
                if ("SEMINAR".equals(type)) speakerName = extra;
                if ("CONCERT".equals(type)) ageRestriction = extra;
            }

            LocalDateTime dt = parseDateTime(dateStr);

            Event e;
            switch (type) {
                case "CONCERT": e = new Concert(id, title, dt, location, capacity, ageRestriction); break;
                case "SEMINAR": e = new Seminar(id, title, dt, location, capacity, speakerName); break;
                case "WORKSHOP": e = new Workshop(id, title, dt, location, capacity, topic); break;
                default: e = new Event(id, title, dt, location, capacity);
            }

            if (statusStr.equals("CANCELLED")) {
                e.cancel();
            }
            events.add(e);
        }
    }

    public void updateFile() throws Exception {
        ArrayList<String> out = new ArrayList<>();

        // Keep CSV output aligned with assignment starter schema.
        out.add("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        for (Event e : events) {
            String eventType = e.getClass().getSimpleName();
            String topic = "";
            String speakerName = "";
            String ageRestriction = "";

            if (e instanceof Workshop) topic = ((Workshop)e).getTopic();
            if (e instanceof Seminar) speakerName = ((Seminar)e).getSpeakerName();
            if (e instanceof Concert) ageRestriction = ((Concert)e).getAgeRestriction();

            String status = e.getStatus() == EventStatus.CANCELLED ? "Cancelled" : "Active";

            out.add(String.join(",",
                    safe(e.getEventId()),
                    safe(e.getTitle()),
                    safe(e.getDateTime().format(fmt)),
                    safe(e.getLocation()),
                    Integer.toString(e.getCapacity()),
                    safe(status),
                    safe(eventType),
                    safe(topic),
                    safe(speakerName),
                    safe(ageRestriction)
            ));
        }
        CsvUtil.writeAll(eventsCsvPath, out);
    }

    private String safe(String v) { return v == null ? "" : v.replace(","," "); }
    private int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
    private LocalDateTime parseDateTime(String s) {
        try { return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
        catch (Exception ex) {
            try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); }
            catch (Exception ignored) { return LocalDateTime.now(); }
        }
    }

    private boolean isKnownEventType(String value) {
        if (value == null) return false;
        String normalized = value.trim().toUpperCase();
        return normalized.equals("WORKSHOP") || normalized.equals("SEMINAR") || normalized.equals("CONCERT");
    }

    public List<Event> searchByTitle(String keyword)
    {
        //List to store matching events
        List<Event> results = new ArrayList<>();

        //Checks all events
        for (Event event : events) {
            //If empty, its like show all
            if (keyword == null || keyword.trim().isEmpty()) {
                results.add(event);

                //Checks if event title has the keyword
            } else if (event.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(event);
            }
        }

        //returns list of matching events
        return results;

    }

    public List<Event> searchByType(String type)
    {
        //New List to store
        List<Event> results = new ArrayList<>();

        //Checks all events
        for (Event event : events) {
            //Checks if empty, if so, shows all
            if (type == null || type.trim().isEmpty() || type.equalsIgnoreCase("All")) {
                results.add(event);
                //Checks for a match in event type
            } else if (event.getClass().getSimpleName().equalsIgnoreCase(type)) {
                results.add(event);
            }
        }

        //Returns matches
        return results;
    }

    public List<Event> searchAndFilter(String keyword, String type)
    {
        //New list
        List<Event> results = new ArrayList<>();

        //Checks all events
        for (Event event : events) {
            //Becomes true if title matches keyword
            boolean matchesTitle = (keyword == null || keyword.trim().isEmpty()) ||
                    event.getTitle().toLowerCase().contains(keyword.toLowerCase());

            //Becomes true if type matches the chosen type
            boolean matchesType = (type == null || type.trim().isEmpty() || type.equalsIgnoreCase("All")) ||
                    event.getClass().getSimpleName().equalsIgnoreCase(type);

            //Adds to event ONLY if both are true(both matches)
            if (matchesTitle && matchesType) {
                results.add(event);
            }
        }

        //Returns the final list
        return results;
    }

}
