package ca.g26final.model.bookings;

import ca.g26final.model.events.Event;
import ca.g26final.model.events.EventStatus;
import ca.g26final.model.users.User;
//1c enterprise
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingManager {

    // Stores all bookings in the system (w status such as confirmed, waitlisted or cancelled)
    private ArrayList<Booking> bookings;

    //counter to generate booking IDs
    private int nextBookingNumber;

    public BookingManager() {
        bookings = new ArrayList<>();
        nextBookingNumber = 1;
    }

    //Generates a booking ID by incrementing a number
    private String generateBookingId() {
        String bookingId = "B" + nextBookingNumber;
        nextBookingNumber++;
        return bookingId;
    }

    //Creates a new booking between a user and an event
    public Booking createBooking(User user, Event event) {

        //makes sure that inputs are correct
        if (user == null) {
            System.out.println("[BookingManager] createBooking failed: user is null");
            return null;
        }
        if (event == null) {
            System.out.println("[BookingManager] createBooking failed: event is null");
            return null;
        }
        if (event.getStatus() == EventStatus.CANCELLED) {
            System.out.println("[BookingManager] createBooking failed: event is cancelled");
            return null;
        }

        //prevents duplicate bookings
        for (Booking b : bookings) {
            if (b.getUserId().equals(user.getUserId())
                    && b.getEventId().equals(event.getEventId())
                    && b.isActive()) {
                System.out.println("[BookingManager] createBooking failed: duplicate active booking (user already booked this event)");
                return null;
            }
        }

        //The confirmed booking limit depends on the user type (Student/Staff/Guest)
        int confirmedForUser = countConfirmedBookingsForUser(user.getUserId());
        if (confirmedForUser >= user.getMaxConfirmedBookings()) {
            System.out.println("[BookingManager] createBooking failed: user reached max confirmed bookings");
            return null;
        }


        BookingStatus status;
        //if even has space, then confirm
        if (countConfirmedBookingsForEvent(event.getEventId()) < event.getCapacity()) {
            status = BookingStatus.CONFIRMED;
        } //if not, waitlisted
        else {
            status = BookingStatus.WAITLISTED;
        }


        Booking booking = new Booking(
                generateBookingId(),
                user.getUserId(),
                event.getEventId(),
                LocalDateTime.now(),
                status
        );

        bookings.add(booking);
        return booking;
    }

    //Cancels booking
    public boolean  cancelBooking(String bookingId) {

        //BOoking failure safeguard
        if (bookingId == null || bookingId.isBlank()) {
            System.out.println("[BookingManager] cancelBooking failed: bookingId blank");
            return false;
        }

        //
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {

                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                    return true;
                }

                boolean wasConfirmed = booking.getBookingStatus() == BookingStatus.CONFIRMED;
                String eventId = booking.getEventId();

                booking.setBookingStatus(BookingStatus.CANCELLED);

                if (wasConfirmed) {
                    promoteFirstWaitlistedBooking(eventId);
                }
                return true;
            }
        }

        return true;
    }

    //
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

    public ArrayList<Booking> getBookingsForUser(String userId) {
        ArrayList<Booking> result = new ArrayList<>();


        if (userId == null || userId.isBlank()) return result;

        for (Booking booking : bookings) {
            if (booking.getUserId().equals(userId)) {
                result.add(booking);
            }
        }

        return result;
    }

    public ArrayList<Booking> getBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)) {
                result.add(booking);
            }
        }

        return result;
    }

    public ArrayList<Booking> getConfirmedBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                result.add(booking);
            }
        }

        return result;
    }

    public ArrayList<Booking> getWaitlistedBookingsForEvent(String eventId) {
        ArrayList<Booking> result = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.WAITLISTED) {
                result.add(booking);
            }
        }

        return result;
    }

    public int countConfirmedBookingsForEvent(String eventId) {
        int count = 0;

        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    public int countConfirmedBookingsForUser(String userId) {
        int count = 0;

        for (Booking booking : bookings) {
            if (booking.getUserId().equals(userId)
                    && booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                count++;
            }
        }

        return count;
    }

    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }

    // Cancels all bookings for an event without promoting from waitlist. Used this when an event is cancelled.
    public int cancelAllBookingsForEventNoPromotion(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            System.out.println("[BookingManager] cancelAllBookingsForEvent_NoPromotion failed: eventId blank");
            return 0;
        }

        int changed = 0;

        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() != BookingStatus.CANCELLED) {
                booking.setBookingStatus(BookingStatus.CANCELLED);
                changed++;
            }
        }

        return changed;
    }
}
