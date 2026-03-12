package ca.g26final.model.bookings;

import java.time.LocalDateTime;

public class Booking {

    // Stores the unique ID for this booking
    private String bookingId;

    // Stores the ID of the user who made the booking
    private String userId;

    // Stores the ID of the event being booked
    private String eventId;

    // Stores the date and time when the booking was created
    private LocalDateTime createdAt;

    // Stores the current status of the booking
    private BookingStatus bookingStatus;

    // Constructor: used to create a new Booking object
    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt, BookingStatus bookingStatus) {

        // This checks if all the data passed in is valid
        boolean valid = true;

        // Check if bookingId is missing or empty
        if (bookingId == null || bookingId.isBlank()) {
            System.out.println("[Booking] Invalid bookingId. Using 'INVALID'.");
            this.bookingId = "INVALID";
            valid = false;
        } else {
            this.bookingId = bookingId;
        }

        // Check if userId is missing or empty
        if (userId == null || userId.isBlank()) {
            System.out.println("[Booking] Invalid userId. Using 'UNKNOWN_USER'.");
            this.userId = "UNKNOWN_USER";
            valid = false;
        } else {
            this.userId = userId;
        }

        // Check if eventId is missing or empty
        if (eventId == null || eventId.isBlank()) {
            System.out.println("[Booking] Invalid eventId. Using 'UNKNOWN_EVENT'.");
            this.eventId = "UNKNOWN_EVENT";
            valid = false;
        } else {
            this.eventId = eventId;
        }

        // Check if createdAt is null
        if (createdAt == null) {
            System.out.println("[Booking] Invalid createdAt. Using now().");
            this.createdAt = LocalDateTime.now(); // uses the current date and time
            valid = false;
        } else {
            this.createdAt = createdAt;
        }

        // Check if bookingStatus is null
        if (bookingStatus == null) {
            System.out.println("[Booking] Invalid bookingStatus. Using CANCELLED.");
            this.bookingStatus = BookingStatus.CANCELLED;
            valid = false;
        } else {
            this.bookingStatus = bookingStatus;
        }

        // If anything was invalid, make sure the booking becomes CANCELLED
        if (!valid && this.bookingStatus != BookingStatus.CANCELLED) {
            this.bookingStatus = BookingStatus.CANCELLED;
        }
    }

    // Returns the booking ID
    public String getBookingId() {
        return bookingId;
    }

    // Returns the user ID
    public String getUserId() {
        return userId;
    }

    // Returns the event ID
    public String getEventId() {
        return eventId;
    }

    // Returns the date and time the booking was created
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Returns the current booking status
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    // Checks if the booking is still active
    // Active means CONFIRMED or WAITLISTED
    public boolean isActive() {
        return bookingStatus == BookingStatus.CONFIRMED || bookingStatus == BookingStatus.WAITLISTED;
    }

    // Updates the booking status
    public void setBookingStatus(BookingStatus bookingStatus) {
        // If the new status is null, do not change anything
        if (bookingStatus == null) {
            return;
        }
        this.bookingStatus = bookingStatus;
    }

    // Converts the Booking object into a readable string
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", createdAt=" + createdAt +
                ", bookingStatus=" + bookingStatus +
                '}';  //returns all important aspects of this class. 
    }
}






