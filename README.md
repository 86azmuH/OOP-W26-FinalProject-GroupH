Campus Event Booking System – Phase 1 Clarified Implementation Plan

PROJECT SUMMARY:
We are building a Java Campus Event Booking System with
a GUI. The system must allow an admin to: - Create users (Student,
Staff, Guest) - Create events (Workshop, Seminar, Concert) - Book users
into events - Cancel bookings - Manage waitlists - Automatically promote
waitlisted users when a confirmed booking is cancelled

This is Phase 1 (Core System). We are NOT implementing search/filter,
full persistence, or JUnit testing in Phase 1.

CORE DESIGN DECISIONS We are using ArrayList-based storage only. We are
NOT using Maps. We are NOT using a Repository pattern. All system data
will be stored inside BookingService.

FOLDER STRUCTURE src/ca/g26final model/ bookings/ events/ users/
service/ BookingService.java EventService.java UserService.java ui/
App.java

DATA STORAGE DESIGN Inside BookingService: - ArrayList users - ArrayList
events - ArrayList bookings

REQUIRED MODEL CLASSES

1.  USER HIERARCHY Abstract class User Fields:

-   userId (String)
-   name (String)
-   email (String) Methods:
-   getters
-   toString()
-   abstract int getMaxConfirmedBookings()

Student extends User - getMaxConfirmedBookings() returns 3

Staff extends User - returns 5

Guest extends User - returns 1

2.  EVENT HIERARCHY Abstract class Event Fields:

-   eventId
-   title
-   dateTime (LocalDateTime)
-   location
-   capacity (> 0)
-   status (EventStatus) Methods:
-   getters
-   cancel()
-   isActive()

Workshop extends Event - topic

Seminar extends Event - speakerName

Concert extends Event - ageRestriction (display only)

3.  BOOKING CLASS Fields:

-   bookingId
-   userId
-   eventId
-   createdAt (LocalDateTime)
-   status (BookingStatus)

BookingStatus values: - CONFIRMED - WAITLISTED - CANCELLED

BOOKING RULES

When booking: 1. User must exist 2. Event must exist and be ACTIVE 3. No
duplicate booking for same user/event 4. Confirmed booking limits:
Student: 3 Staff: 5 Guest: 1 5. If confirmed count < capacity →
CONFIRMED Else → WAITLISTED

CANCEL BOOKING RULES

If WAITLISTED: - Set status to CANCELLED

If CONFIRMED: - Set status to CANCELLED - Promote earliest WAITLISTED
booking for that event

CANCEL EVENT RULES - Set event status to CANCELLED - Cancel all
CONFIRMED and WAITLISTED bookings for that event

SERVICE RESPONSIBILITIES

BookingService: - Stores users, events, bookings - addUser() -
addEvent() - bookEvent() - cancelBooking() - getBookingsForUser() -
getEvents() - getUsers()

UserService: - Creates User objects - Validates unique userId - Calls
bookingService.addUser()

EventService: - Creates Event objects - Validates unique eventId and
capacity - Calls bookingService.addEvent()

GUI REQUIREMENTS (Phase 1) Must allow: - Add User - List Users - Add
Event - List Events - Book Event - Cancel Booking - View event roster
(confirmed + waitlisted)

REQUIRED DEMO SCENARIO 1. Create event with capacity = 1 2. Book User A
→ CONFIRMED 3. Book User B → WAITLISTED 4. Cancel User A booking 5. User
B becomes CONFIRMED automatically

IMPLEMENTATION ORDER 1. Create enums 2. Create User hierarchy 3. Create
Event hierarchy 4. Create Booking class 5. Implement BookingService
(ArrayLists) 6. Implement UserService and EventService 7. Build GUI 8.
Test promotion logic

