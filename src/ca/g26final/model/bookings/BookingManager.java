package ca.g26final.model.bookings;

import ca.g26final.model.events.Event;
import ca.g26final.model.events.EventStatus;
import ca.g26final.model.users.User;
import ca.g26final.persistence.CsvUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;

public class BookingManager {

    // Stores all bookings in the system
    // A booking can be confirmed, waitlisted, or cancelled
    private ArrayList<Booking> bookings;

    // Keeps track of the next booking number for making booking IDs
    private int nextBookingNumber;

    // Constructor: starts with an empty booking list
    // and sets the first booking number to 1
    public BookingManager() {
        bookings = new ArrayList<>();
        nextBookingNumber = 1;
    }

    // Creates a new booking ID like B1, B2, B3...
    private String generateBookingId() {
        String bookingId = "B" + nextBookingNumber;
        nextBookingNumber++;
        return bookingId;
    }

    // Creates a new booking for a user and an event
    public Booking createBooking(User user, Event event) {

        // Check if the user is missing
        if (user == null) {
            System.out.println("[BookingManager] createBooking failed: user is null");
            return null;
        }

        // Check if the event is missing
        if (event == null) {
            System.out.println("[BookingManager] createBooking failed: event is null");
            return null;
        }

        // Do not allow booking if the event is cancelled
        if (event.getStatus() == EventStatus.CANCELLED) {
            System.out.println("[BookingManager] createBooking failed: event is cancelled");
            return null;
        }

        // Stop the same user from making the same active booking twice
        for (Booking b : bookings) {
            if (b.getUserId().equals(user.getUserId())
                    && b.getEventId().equals(event.getEventId())
                    && b.isActive()) {
                System.out.println("[BookingManager] createBooking failed: duplicate active booking (user already booked this event)");
                return null;
            }
        }

        // Count how many confirmed bookings this user already has
        int confirmedForUser = countConfirmedBookingsForUser(user.getUserId());

        // If the user reached their limit, do not allow another confirmed booking
        if (confirmedForUser >= user.getMaxConfirmedBookings()) {
            System.out.println("[BookingManager] createBooking failed: user reached max confirmed bookings");
            return null;
        }

        BookingStatus status;

        // If the event still has space, the booking is confirmed
        if (countConfirmedBookingsForEvent(event.getEventId()) < event.getCapacity()) {
            status = BookingStatus.CONFIRMED;
        }
        // If the event is full, the booking goes on the waitlist
        else {
            status = BookingStatus.WAITLISTED;
        }

        // Create the new booking object
        Booking booking = new Booking(
                generateBookingId(),
                user.getUserId(),
                event.getEventId(),
                LocalDateTime.now(),
                status
        );

        // Add the new booking to the list
        bookings.add(booking);

        // Return the booking that was created
        return booking;
    }

    // Cancels a booking using its booking ID
    public boolean cancelBooking(String bookingId) {

        // Check if the booking ID is missing or blank
        if (bookingId == null || bookingId.isBlank()) {
            System.out.println("[BookingManager] cancelBooking failed: bookingId blank");
            return false;
        }

        // Look through all bookings to find the matching booking
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {

                // If it is already cancelled, nothing else needs to be done
                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                    return true;
                }

                // Remember if this booking was confirmed before cancelling it
                boolean wasConfirmed = booking.getBookingStatus() == BookingStatus.CONFIRMED;
                String eventId = booking.getEventId();

                // Change the booking status to cancelled
                booking.setBookingStatus(BookingStatus.CANCELLED);

                // If a confirmed booking was cancelled,
                // move the first waitlisted person into confirmed
                if (wasConfirmed) {
                    promoteFirstWaitlistedBooking(eventId);
                }
                return true;
            }
        }
        return false;
    }

    // Finds the first waitlisted booking for an event
    // and changes it to confirmed
    private void promoteFirstWaitlistedBooking(String eventId) {
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.WAITLISTED) {
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                System.out.println("Promoted from waitlist: " + booking.getBookingId());
                return;
            }
        }
    }

    // Returns all bookings made by one user
    public ArrayList<Booking> getBookingsForUser(String userId) {
        ArrayList<Booking> result = new ArrayList<>();

        // If userId is invalid, return an empty list
        if (userId == null || userId.isBlank()) return result;

        // Add all bookings that belong to this user
        for (Booking booking : bookings) {
            if (booking.getUserId().equals(userId)) {
                result.add(booking);
            }
        }

        return result;
    }

    // Returns all bookings for one event
    public ArrayList<Booking> getBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        // Add all bookings that belong to this event
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)) {
                result.add(booking);
            }
        }

        return result;
    }

    // Returns only confirmed bookings for one event
    public ArrayList<Booking> getConfirmedBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        // Add only confirmed bookings for this event
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                result.add(booking);
            }
        }

        return result;
    }

    // Returns only waitlisted bookings for one event
    public ArrayList<Booking> getWaitlistedBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        // Add only waitlisted bookings for this event
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.WAITLISTED) {
                result.add(booking);
            }
        }

        return result;
    }

    // Counts how many confirmed bookings an event has
    public int countConfirmedBookingsForEvent(String eventId) {
        int count = 0;

        // Go through all bookings and count confirmed ones for this event
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    // Counts how many confirmed bookings a user has
    public int countConfirmedBookingsForUser(String userId) {
        int count = 0;

        // Go through all bookings and count confirmed ones for this user
        for (Booking booking : bookings) {
            if (booking.getUserId().equals(userId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    // Returns the full list of all bookings
    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }

    // Cancels every booking for one event
    // This is useful if the event itself gets cancelled
    // It does not promote anyone from the waitlist
    public int cancelAllBookingsForEventNoPromotion(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            System.out.println("[BookingManager] cancelAllBookingsForEvent_NoPromotion failed: eventId blank");
            return 0;
        }

        int changed = 0;

        // Go through all bookings for this event
        // and cancel any booking that is not already cancelled
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() != BookingStatus.CANCELLED) {
                booking.setBookingStatus(BookingStatus.CANCELLED);
                changed++;
            }
        }

        // Return how many bookings were changed
        return changed;
    }

    // Persistence for bookings
    // CSV: bookingId,userId,eventId,createdAtISO,status
    public void loadFromCsv(Path path) throws Exception {
        List<String> lines = CsvUtil.readAll(path);
        bookings.clear();
        int maxNum = 0;
        for (String line : lines) {
            String[] p = line.split(",", -1);
            if (p.length < 5) continue;

            // Skip header rows
            if (p[0].trim().equalsIgnoreCase("bookingId")) {
                continue;
            }

            String bid = p[0].trim();
            String uid = p[1].trim();
            String eid = p[2].trim();
            String createdStr = p[3].trim();
            String statusStr = p[4].trim().toUpperCase();

            LocalDateTime created;
            try { created = LocalDateTime.parse(createdStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME); }
            catch (Exception ex) {
                try { created = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")); }
                catch (Exception ignored) { created = LocalDateTime.now(); }
            }

            BookingStatus st;
            try { st = BookingStatus.valueOf(statusStr); } catch (Exception ex) { st = BookingStatus.CANCELLED; }

            bookings.add(new Booking(bid, uid, eid, created, st));

            if (bid.startsWith("B")) {
                try {
                    int n = Integer.parseInt(bid.substring(1));
                    if (n > maxNum) maxNum = n;
                } catch (Exception ignored) {}
            }
        }
        nextBookingNumber = Math.max(1, maxNum + 1);
    }

    public void updateFile(Path path) throws Exception {
        ArrayList<String> out = new ArrayList<>();

        // Keep CSV output aligned with assignment starter schema.
        out.add("bookingId,userId,eventId,createdAt,bookingStatus");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        for (Booking b : bookings) {
            String status = toTitleCaseStatus(b.getBookingStatus());
            out.add(String.join(",",
                    safe(b.getBookingId()),
                    safe(b.getUserId()),
                    safe(b.getEventId()),
                    safe(b.getCreatedAt().format(fmt)),
                    safe(status)
            ));
        }
        CsvUtil.writeAll(path, out);
    }

    private String safe(String v) { return v == null ? "" : v.replace(","," "); }

    private String toTitleCaseStatus(BookingStatus status) {
        if (status == null) return "Cancelled";
        switch (status) {
            case CONFIRMED: return "Confirmed";
            case WAITLISTED: return "Waitlisted";
            default: return "Cancelled";
        }
    }
}

