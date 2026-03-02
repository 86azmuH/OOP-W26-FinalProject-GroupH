package ca.g26final.model.bookings;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;

    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt, BookingStatus bookingStatus) {

        boolean valid = true;

        if (bookingId == null || bookingId.isBlank()) {
            System.out.println("[Booking] Invalid bookingId. Using 'INVALID'.");
            this.bookingId = "INVALID";
            valid = false;
        } else {
            this.bookingId = bookingId;
        }

        if (userId == null || userId.isBlank()) {
            System.out.println("[Booking] Invalid userId. Using 'UNKNOWN_USER'.");
            this.userId = "UNKNOWN_USER";
            valid = false;
        } else {
            this.userId = userId;
        }

        if (eventId == null || eventId.isBlank()) {
            System.out.println("[Booking] Invalid eventId. Using 'UNKNOWN_EVENT'.");
            this.eventId = "UNKNOWN_EVENT";
            valid = false;
        } else {
            this.eventId = eventId;
        }

        if (createdAt == null) {
            System.out.println("[Booking] Invalid createdAt. Using now().");
            this.createdAt = LocalDateTime.now();
            valid = false;
        } else {
            this.createdAt = createdAt;
        }

        if (bookingStatus == null) {
            System.out.println("[Booking] Invalid bookingStatus. Using CANCELLED.");
            this.bookingStatus = BookingStatus.CANCELLED;
            valid = false;
        } else {
            this.bookingStatus = bookingStatus;
        }

        if (!valid && this.bookingStatus != BookingStatus.CANCELLED) {
            this.bookingStatus = BookingStatus.CANCELLED;
        }
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public boolean isActive() {
        return bookingStatus == BookingStatus.CONFIRMED || bookingStatus == BookingStatus.WAITLISTED;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        if (bookingStatus == null) {
            System.out.println("[Booking] Status not updated");
            return;
        }
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", createdAt=" + createdAt +
                ", bookingStatus=" + bookingStatus +
                '}';
    }
}
