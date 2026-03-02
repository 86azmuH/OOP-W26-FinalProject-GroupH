package ca.g26final.model.bookings;
// This enum stores the possible states of a booking.
// CONFIRMED = user got a spot in the event
// WAITLISTED = event is full, so user is waiting for a spot
// CANCELLED = the booking was removed/cancelled
public enum BookingStatus {
CONFIRMED,
    WAITLISTED,
    CANCELLED
}

