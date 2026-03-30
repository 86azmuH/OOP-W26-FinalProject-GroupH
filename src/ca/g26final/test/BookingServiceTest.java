/*
 * BookingServiceTest //bassically creates test cases to make sure our service works. Need to download Junit stuff in project structure to run
 *
 * This class tests the main booking rules of the event system using junit test from maven
 * Instead of running the whole application manually, these tests create a small
 * sample setup with users and events, perform booking actions, and then check
 * whether the results are correct.
 *
 * A fresh system is created before each test using @BeforeEach. This is important
 * because it keeps the tests independent from one another. If one test changes
 * the booking data, that change should not affect the next test.
 *
 * The test cases cover four important behaviours:
 *
 * 1. Booking under capacity
 *    This checks that when an event still has space, the booking is created
 *    successfully and the status becomes CONFIRMED.
 *
 * 2. Booking when full
 *    This checks that once the event reaches capacity, the next user is not
 *    given a confirmed spot and is placed on the WAITLIST instead.
 *
 * 3. Cancelling a confirmed booking
 *    This checks that when a confirmed booking is cancelled, the first person
 *    on the waitlist is promoted into the confirmed list.
 *
 * 4. Duplicate booking prevention
 *    This checks that the same user cannot actively book the same event twice.
 *    The first booking should succeed, and the second attempt should fail.
 *
 * These tests help prove that the booking logic works correctly and consistently.
 * They are especially useful after making code changes, because they quickly show
 * whether any core booking rule has been broken.
 */













package ca.g26final.test;

import ca.g26final.model.bookings.Booking;
import ca.g26final.model.bookings.BookingStatus;
import ca.g26final.model.events.Event;
import ca.g26final.model.users.Student;
import ca.g26final.service.BookingService;
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        userService = new UserService(Path.of("test-users.csv"));
        eventService = new EventService(Path.of("test-events.csv"));
        bookingService = new BookingService(userService, eventService);
    }

    @Test
    void bookingUnderCapacity_shouldCreateConfirmedBooking() {
        Student student = new Student("U1", "Alice", "alice@test.com");
        Event event = new Event(
                "E1",
                "Java Workshop",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                "Room 101",
                2
        );

        assertTrue(userService.addUser(student));
        assertTrue(eventService.addEvent(event));

        Booking booking = bookingService.bookEvent("U1", "E1");

        assertNotNull(booking);
        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());
        assertEquals(1, bookingService.getConfirmedRoster("E1").size());
        assertEquals(0, bookingService.getWaitlist("E1").size());
    }

    @Test
    void bookingWhenFull_shouldPutSecondUserOnWaitlist() {
        Student student1 = new Student("U1", "Alice", "alice@test.com");
        Student student2 = new Student("U2", "Bob", "bob@test.com");
        Event event = new Event(
                "E1",
                "Java Workshop",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                "Room 101",
                1
        );

        assertTrue(userService.addUser(student1));
        assertTrue(userService.addUser(student2));
        assertTrue(eventService.addEvent(event));

        Booking firstBooking = bookingService.bookEvent("U1", "E1");
        Booking secondBooking = bookingService.bookEvent("U2", "E1");

        assertNotNull(firstBooking);
        assertNotNull(secondBooking);
        assertEquals(BookingStatus.CONFIRMED, firstBooking.getBookingStatus());
        assertEquals(BookingStatus.WAITLISTED, secondBooking.getBookingStatus());
        assertEquals(1, bookingService.getConfirmedRoster("E1").size());
        assertEquals(1, bookingService.getWaitlist("E1").size());
    }

    @Test
    void cancelConfirmedBooking_shouldPromoteFirstWaitlistedBooking() {
        Student student1 = new Student("U1", "Alice", "alice@test.com");
        Student student2 = new Student("U2", "Bob", "bob@test.com");
        Event event = new Event(
                "E1",
                "Java Workshop",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                "Room 101",
                1
        );

        assertTrue(userService.addUser(student1));
        assertTrue(userService.addUser(student2));
        assertTrue(eventService.addEvent(event));

        Booking firstBooking = bookingService.bookEvent("U1", "E1");
        Booking secondBooking = bookingService.bookEvent("U2", "E1");

        assertNotNull(firstBooking);
        assertNotNull(secondBooking);
        assertEquals(BookingStatus.CONFIRMED, firstBooking.getBookingStatus());
        assertEquals(BookingStatus.WAITLISTED, secondBooking.getBookingStatus());

        assertTrue(bookingService.cancelBooking(firstBooking.getBookingId()));

        Booking cancelledBooking = bookingService.getBookingsForUser("U1").get(0);
        Booking promotedBooking = bookingService.getBookingsForUser("U2").get(0);

        assertEquals(BookingStatus.CANCELLED, cancelledBooking.getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED, promotedBooking.getBookingStatus());
        assertEquals(1, bookingService.getConfirmedRoster("E1").size());
        assertEquals(0, bookingService.getWaitlist("E1").size());
    }

    @Test
    void duplicateBooking_shouldBePrevented() {
        Student student = new Student("U1", "Alice", "alice@test.com");
        Event event = new Event(
                "E1",
                "Java Workshop",
                LocalDateTime.of(2026, 4, 1, 10, 0),
                "Room 101",
                2
        );

        assertTrue(userService.addUser(student));
        assertTrue(eventService.addEvent(event));

        Booking firstBooking = bookingService.bookEvent("U1", "E1");
        Booking secondBooking = bookingService.bookEvent("U1", "E1");

        assertNotNull(firstBooking);
        assertNull(secondBooking);
        assertEquals(1, bookingService.getBookingsForUser("U1").size());
        assertEquals(1, bookingService.getConfirmedRoster("E1").size());
    }
}
