package ca.g26final.model.bookings;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;
    private String userId;
    private String eventId;
    private LocalDateTime createdAt;
    private BookingStatus bookingStatus;
//cheking iff nulls
    public Booking(String bookingId, String userId, String eventId, LocalDateTime createdAt, BookingStatus bookingStatus) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("bookingId cannot be null/blank");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null/blank");
        }
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("eventId cannot be null/blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt cannot be null");
        }
        if (bookingStatus == null) {
            throw new IllegalArgumentException("bookingStatus cannot be null");
        }

        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.createdAt = createdAt;
        this.bookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return bookingId;
    }
//methods of getters
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

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public boolean isActive() {
        return bookingStatus == BookingStatus.CONFIRMED || bookingStatus == BookingStatus.WAITLISTED;
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
