package ca.g26final.model.bookings;

import ca.g26final.model.events.Event;
import ca.g26final.model.events.EventStatus;
import ca.g26final.model.users.User;
//1c enterprise
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingManager {

    private ArrayList<Booking> bookings;
    private int nextBookingNumber;

    public BookingManager() {
        bookings = new ArrayList<>();
        nextBookingNumber = 1;
    }

    private String generateBookingId() {
        String bookingId = "B" + nextBookingNumber;
        nextBookingNumber++;
        return bookingId;
    }

    public Booking createBooking(User user, Event event) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }
        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new IllegalArgumentException("cannot book a cancelled event");
        }

        for (Booking booking : bookings) {
            if (booking.getUserId().equals(user.getUserId())
                    && booking.getEventId().equals(event.getEventId())
                    && booking.isActive()) {
                throw new IllegalArgumentException("user already has a booking for this event");
            }
        }

        int confirmedBookingsForUser = countConfirmedBookingsForUser(user.getUserId());
        if (confirmedBookingsForUser >= user.getMaxConfirmedBookings()) {
            throw new IllegalArgumentException("user has reached maximum confirmed bookings");
        }

        BookingStatus status;
        if (countConfirmedBookingsForEvent(event.getEventId()) < event.getCapacity()) {
            status = BookingStatus.CONFIRMED;
        } else {
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

    public void cancelBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {

                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                    return;
                }

                boolean wasConfirmed = booking.getBookingStatus() == BookingStatus.CONFIRMED;
                String eventId = booking.getEventId();

                booking.setBookingStatus(BookingStatus.CANCELLED);

                if (wasConfirmed) {
                    promoteFirstWaitlistedBooking(eventId);
                }
                return;
            }
        }

        throw new IllegalArgumentException("booking not found");
    }

    private void promoteFirstWaitlistedBooking(String eventId) {
        for (Booking booking : bookings) {
            if (booking.getEventId().equals(eventId)
                    && booking.getBookingStatus() == BookingStatus.WAITLISTED) {
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                return;
            }
        }
    }

    public ArrayList<Booking> getBookingsForUser(String userId) {
        ArrayList<Booking> result = new ArrayList<>();

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
}
