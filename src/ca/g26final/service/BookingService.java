package ca.g26final.service;

import ca.g26final.model.bookings.Booking;
import ca.g26final.model.bookings.BookingManager;
import ca.g26final.model.events.Event;
import ca.g26final.model.users.User;

import java.util.ArrayList;

public class BookingService {

    private UserService userService;
    private EventService eventService;
    private BookingManager bookingManager;

    // constructor for booking service
    public BookingService(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
        this.bookingManager = new BookingManager();
    }

    // Books an event using IDs (returns Booking or null if failed)
    public Booking bookEvent(String userId, String eventId) {

        if (userId == null || userId.isBlank()) {
            System.out.println("[BookingService] bookEvent failed: userId blank");
            return null;
        }

        if (eventId == null || eventId.isBlank()) {
            System.out.println("[BookingService] bookEvent failed: eventId blank");
            return null;
        }

        User user = userService.getUserById(userId.trim());
        if (user == null) {
            System.out.println("[BookingService] bookEvent failed: user not found " + userId);
            return null;
        }

        Event event = eventService.getEventById(eventId.trim());
        if (event == null) {
            System.out.println("[BookingService] bookEvent failed: event not found " + eventId);
            return null;
        }

        // BookingManager does all booking rules. It returns null if it fails.
        return bookingManager.createBooking(user, event);
    }

    // Cancels a booking with a bookingId. Returns true if cancelled or already
    // cancelled, false otherwise.
    public boolean cancelBooking(String bookingId) {
        return bookingManager.cancelBooking(bookingId);
    }

    // Returns all bookings for a user
    public ArrayList<Booking> getBookingsForUser(String userId) {
        return bookingManager.getBookingsForUser(userId);
    }

    // Returns all bookings for an event
    public ArrayList<Booking> getBookingsForEvent(String eventId) {
        return bookingManager.getBookingsForEvent(eventId);
    }

    // Returns confirmed roster for an event
    public ArrayList<Booking> getConfirmedRoster(String eventId) {
        return bookingManager.getConfirmedBookingsForEvent(eventId);
    }

    // Returns waitlist for an event
    public ArrayList<Booking> getWaitlist(String eventId) {
        return bookingManager.getWaitlistedBookingsForEvent(eventId);
    }

    public boolean cancelEventAndCancelBookings(String eventId) {

        if (eventId == null || eventId.isBlank()) {
            System.out.println("[BookingService] cancelEventAndCancelBookings failed: eventId blank");
            return false;
        }

        Event event = eventService.getEventById(eventId.trim());
        if (event == null) {
            System.out.println("[BookingService] cancelEventAndCancelBookings failed: event not found " + eventId);
            return false;
        }

        // Cancel the event
        event.cancel();

        // cancel all bookings for this event without promoting anyone
        int cancelledCount = bookingManager.cancelAllBookingsForEventNoPromotion(event.getEventId());

        // Optional message for debugging
        System.out.println("[BookingService] Event cancelled. Bookings cancelled: " + cancelledCount);

        return true;
    }

    // Cancel a waitlisted booking as well (same cancelBooking)
    public boolean cancelWaitlistedBooking(String bookingId) {
        return cancelBooking(bookingId);
    }

    // expose all bookings for UI purposes
    public ArrayList<Booking> getAllBookings() {
        return bookingManager.getAllBookings();
    }

}
