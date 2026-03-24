# Campus Event Booking System

## Project Description

This project is the final assignment for the OOP Winter 2026 course. It implements a simple Java desktop application for managing campus events, users, and bookings. The system allows administrators to create and manage users (Students, Staff, Guests), events (Workshops, Seminars, Concerts), and handle bookings with waitlist functionality.

**Phase 1 Focus**: Core logic and basic Swing GUI. This phase intentionally omits persistence, search/filter capabilities, and automated tests, which are planned for future phases.


---

## 🧩 Project Overview

This is a comprehensive event booking management system built in Java using Swing for the GUI. The system allows administrators to manage users, events, and bookings with automatic waitlist promotion functionality.

### Core Features Implemented (Phase 1)
- **User Management**: Create and manage three types of users (Student, Staff, Guest) with different booking limits
- **Event Management**: Create and manage three types of events (Workshop, Seminar, Concert) with capacity limits
- **Booking System**: Book users into events with automatic waitlist management
- **Waitlist Promotion**: Automatic promotion from waitlist when confirmed bookings are cancelled
- **Swing GUI**: Complete graphical interface with tabbed panels for all operations

### Key Business Rules
1. **User Types & Limits**:
   - Students: Maximum 3 confirmed bookings
   - Staff: Maximum 5 confirmed bookings
   - Guests: Maximum 1 confirmed booking

2. **Booking Logic**:
   - Users can only book active events
   - No duplicate bookings for same user/event
   - Bookings are CONFIRMED if capacity allows, otherwise WAITLISTED
   - Confirmed booking cancellation automatically promotes earliest waitlist entry

3. **Event Cancellation**: Cancels all associated bookings and sets event status to CANCELLED

> ⚠️ **Phase 1 Scope**: Intentionally omits search/filter functionality, data persistence, and automated tests. These are planned for future phases.

---

## 📁 Project Structure & File Organization

```
FinalProject/
├── README.md                    # This detailed project documentation
├── bin/                         # Compiled Java classes
│   └── ca/g26final/
│       ├── App.class
│       ├── model/
│       │   ├── bookings/
│       │   ├── events/
│       │   └── users/
│       ├── service/
│       └── ui/
├── lib/                         # External libraries (if any)
└── src/                         # Source code
    └── ca/g26final/
        ├── App.java            # Main application entry point
        ├── model/              # Domain model classes
        │   ├── bookings/
        │   │   ├── Booking.java
        │   │   ├── BookingManager.java
        │   │   └── BookingStatus.java
        │   ├── events/
        │   │   ├── Concert.java
        │   │   ├── Event.java
        │   │   ├── EventStatus.java
        │   │   ├── Seminar.java
        │   │   └── Workshop.java
        │   └── users/
        │       ├── Guest.java
        │       ├── Staff.java
        │       ├── Student.java
        │       └── User.java
        ├── service/            # Business logic layer
        │   ├── BookingService.java
        │   ├── EventService.java
        │   └── UserService.java
        └── ui/                 # User interface layer
            └── MainWindow.java
```

---

## 🏗️ Architecture Overview

The application follows a **Model-View-Controller (MVC)** pattern:

- **Model** (`model/`): Domain objects representing users, events, and bookings
- **View** (`ui/`): Swing-based graphical user interface
- **Controller** (`service/`): Business logic and data management

### Data Storage
- All data is stored in memory using `ArrayList` collections in `BookingService`
- No database persistence in Phase 1
- Simple ID-based relationships between entities

---

## 📋 Detailed Class Documentation

### 🔵 App.java - Application Entry Point
**Location**: `src/ca/g26final/App.java`

**Purpose**: Main class that initializes the application and launches the GUI.

**Key Methods**:
- `main(String[] args)`: Creates service instances and launches Swing GUI on Event Dispatch Thread

**Dependencies**: All service classes and `MainWindow`

---

### 👥 User Model Classes

#### User.java (Abstract Base Class)
**Location**: `src/ca/g26final/model/users/User.java`

**Fields**:
- `String userId`: Unique identifier
- `String name`: User's full name
- `String email`: User's email address

**Abstract Methods**:
- `int getMaxConfirmedBookings()`: Returns maximum allowed confirmed bookings

**Concrete Methods**:
- `getUserId()`, `getName()`, `getEmail()`: Getters
- `toString()`: Returns formatted user information

**Subclasses**: `Student`, `Staff`, `Guest`

#### Student.java
**Location**: `src/ca/g26final/model/users/Student.java`

**Inherits**: `User`
**Booking Limit**: 3 confirmed bookings

**Constructor**: `Student(String userId, String name, String email)`
**Overrides**: `getMaxConfirmedBookings()` returns 3

#### Staff.java
**Location**: `src/ca/g26final/model/users/Staff.java`

**Inherits**: `User`
**Booking Limit**: 5 confirmed bookings

**Constructor**: `Staff(String userId, String name, String email)`
**Overrides**: `getMaxConfirmedBookings()` returns 5

#### Guest.java
**Location**: `src/ca/g26final/model/users/Guest.java`

**Inherits**: `User`
**Booking Limit**: 1 confirmed booking

**Constructor**: `Guest(String userId, String name, String email)`
**Overrides**: `getMaxConfirmedBookings()` returns 1

---

### 🎪 Event Model Classes

#### EventStatus.java (Enum)
**Location**: `src/ca/g26final/model/events/EventStatus.java`

**Values**: `ACTIVE`, `CANCELLED`

#### Event.java (Abstract Base Class)
**Location**: `src/ca/g26final/model/events/Event.java`

**Fields**:
- `String eventId`: Unique identifier
- `String title`: Event title
- `LocalDateTime dateTime`: Event date and time
- `String location`: Event location
- `int capacity`: Maximum attendees
- `EventStatus status`: Current status

**Validation Rules**:
- All string fields must be non-null and non-empty
- Capacity must be positive
- DateTime must be in the future

**Methods**:
- Getters for all fields
- `setTitle(String)`, `setDateTime(LocalDateTime)`, `setLocation(String)`, `setCapacity(int)`: Setters with validation
- `cancel()`: Sets status to CANCELLED
- `isActive()`: Returns true if status is ACTIVE
- `toString()`: Formatted event description

**Subclasses**: `Workshop`, `Seminar`, `Concert`

#### Workshop.java
**Location**: `src/ca/g26final/model/events/Workshop.java`

**Inherits**: `Event`
**Additional Field**: `String topic`

**Constructor**: `Workshop(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String topic)`
**Methods**: `getTopic()`

#### Seminar.java
**Location**: `src/ca/g26final/model/events/Seminar.java`

**Inherits**: `Event`
**Additional Field**: `String speakerName`

**Constructor**: `Seminar(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String speakerName)`
**Methods**: `getSpeakerName()`

#### Concert.java
**Location**: `src/ca/g26final/model/events/Concert.java`

**Inherits**: `Event`
**Additional Field**: `int ageRestriction`

**Constructor**: `Concert(String eventId, String title, LocalDateTime dateTime, String location, int capacity, int ageRestriction)`
**Methods**: `getAgeRestriction()`

---

### 🎫 Booking Model Classes

#### BookingStatus.java (Enum)
**Location**: `src/ca/g26final/model/bookings/BookingStatus.java`

**Values**: `CONFIRMED`, `WAITLISTED`, `CANCELLED`

#### Booking.java
**Location**: `src/ca/g26final/model/bookings/Booking.java`

**Fields**:
- `String bookingId`: Unique identifier
- `String userId`: Reference to user
- `String eventId`: Reference to event
- `LocalDateTime createdAt`: Booking creation timestamp
- `BookingStatus status`: Current booking status

**Validation**: All IDs must be non-null and non-empty

**Methods**:
- Getters for all fields
- `isActive()`: Returns true if status is CONFIRMED or WAITLISTED
- `setBookingStatus(BookingStatus)`: Updates booking status
- `toString()`: Formatted booking description

#### BookingManager.java
**Location**: `src/ca/g26final/model/bookings/BookingManager.java`

**Purpose**: Core booking logic and data management

**Fields**:
- `ArrayList<Booking> bookings`: All bookings
- `AtomicInteger bookingIdCounter`: ID generator

**Key Methods**:
- `String generateBookingId()`: Creates unique booking IDs
- `Booking createBooking(String userId, String eventId, User user, Event event)`: Creates booking with business rules
- `boolean cancelBooking(String bookingId)`: Cancels booking and handles promotion
- `List<Booking> getBookingsByEvent(String eventId)`: Gets all bookings for an event
- `List<Booking> getBookingsByUser(String userId)`: Gets all bookings for a user
- `List<Booking> getActiveBookingsByEvent(String eventId)`: Gets active bookings for event
- `List<Booking> getWaitlistedBookingsByEvent(String eventId)`: Gets waitlisted bookings for event
- `int getConfirmedCount(String eventId)`: Counts confirmed bookings
- `int getWaitlistCount(String eventId)`: Counts waitlisted bookings
- `Optional<Booking> getEarliestWaitlistBooking(String eventId)`: Gets next promotion candidate

**Business Logic**:
- Validates user/event existence and event status
- Prevents duplicate bookings
- Enforces user booking limits
- Manages CONFIRMED vs WAITLISTED status based on capacity
- Handles automatic promotion on cancellation

---

### 🔧 Service Layer Classes

#### UserService.java
**Location**: `src/ca/g26final/service/UserService.java`

**Purpose**: User management and validation

**Fields**:
- `BookingService bookingService`: Reference to main service

**Methods**:
- `void addUser(User user)`: Adds user with ID uniqueness validation
- `Optional<User> getUserById(String userId)`: Retrieves user by ID
- `List<User> getAllUsers()`: Returns all users
- `boolean userExists(String userId)`: Checks if user exists
- `boolean removeUser(String userId)`: Removes user if no active bookings

#### EventService.java
**Location**: `src/ca/g26final/service/EventService.java`

**Purpose**: Event management and validation

**Fields**:
- `BookingService bookingService`: Reference to main service

**Methods**:
- `void addEvent(Event event)`: Adds event with ID uniqueness validation
- `Optional<Event> getEventById(String eventId)`: Retrieves event by ID
- `List<Event> getAllEvents()`: Returns all events
- `boolean updateEvent(Event updatedEvent)`: Updates existing event
- `boolean cancelEvent(String eventId)`: Cancels event and all bookings
- `boolean isEventActive(String eventId)`: Checks if event is active
- `boolean removeEvent(String eventId)`: Removes event if no bookings

#### BookingService.java
**Location**: `src/ca/g26final/service/BookingService.java`

**Purpose**: Main service coordinating all booking operations

**Fields**:
- `ArrayList<User> users`: User storage
- `ArrayList<Event> events`: Event storage
- `BookingManager bookingManager`: Booking logic handler
- `UserService userService`: User operations
- `EventService eventService`: Event operations

**Key Methods**:
- `void addUser(String userId, String name, String email, String userType)`: Creates and adds user
- `void addEvent(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String eventType, String... additionalParams)`: Creates and adds event
- `boolean bookEvent(String userId, String eventId)`: Books user into event
- `boolean cancelBooking(String bookingId)`: Cancels booking
- `List<Booking> getBookingsByEvent(String eventId)`: Gets event bookings
- `List<Booking> getBookingsByUser(String userId)`: Gets user bookings
- `boolean cancelEventAndCancelBookings(String eventId)`: Cancels event and all bookings
- `boolean hasBookings(String eventId)`: Checks if event has bookings
- `List<User> getUsers()`, `List<Event> getEvents()`, `List<Booking> getBookings()`: Data access methods

---

### 🎨 User Interface - MainWindow.java
**Location**: `src/ca/g26final/ui/MainWindow.java`

**Purpose**: Complete Swing-based GUI with tabbed interface

**Architecture**: JFrame with JTabbedPane containing 4 main panels

**Panel Methods**:
- `JPanel createUsersPanel()`: User management (add, list, remove, view details)
- `JPanel createEventsPanel()`: Event management (add, list, cancel, view bookings)
- `JPanel createBookingsPanel()`: Booking operations (book, cancel, view user bookings)
- `JPanel createWaitlistPanel()`: Waitlist management (view waitlists, remove from waitlist)

**Refresh Methods**:
- `void refreshUsers()`: Updates users table
- `void refreshEvents()`: Updates events table
- `void refreshBookings()`: Updates bookings table

**Action Methods**:
- `void addUser()`: Creates new user via dialog
- `void addEvent()`: Creates new event via dialog
- `void bookEvent()`: Books user into event
- `void cancelBooking()`: Cancels booking with promotion logic
- `void viewUserDetails()`: Shows user's booking details
- `void viewWaitlist()`: Shows event waitlist
- `void removeWaitlist()`: Removes user from waitlist
- `void removeUser()`: Removes user (if no bookings)
- `void cancelEvent()`: Cancels event and all bookings

**GUI Components**: JTables for data display, JButtons for actions, JOptionPane for dialogs

---

## 🔄 Core Business Logic Flow

### Booking Process
1. **Validation**: Check user/event exist, event is active, no duplicates, user under limit
2. **Capacity Check**: If confirmed count < capacity → CONFIRMED, else WAITLISTED
3. **Creation**: Generate booking ID, create Booking object, add to collection

### Cancellation Process
1. **Find Booking**: Locate booking by ID
2. **Status Check**: If WAITLISTED → just cancel
3. **If CONFIRMED**: Cancel booking, then promote earliest waitlist entry for same event
4. **Promotion**: Change waitlist booking to CONFIRMED status

### Event Cancellation
1. **Cancel Event**: Set event status to CANCELLED
2. **Cancel All Bookings**: Set all associated bookings to CANCELLED
3. **No Promotion**: Event cancellation doesn't trigger waitlist promotion

---

## 🚀 How to Run the Application

### Prerequisites
- Java 8 or higher installed
- Windows/Linux/macOS environment

### Compilation
```bash
# From project root directory
javac -d bin -cp src src/ca/g26final/*.java src/ca/g26final/*/*.java src/ca/g26final/*/*/*.java
```

### Execution
```bash
# From project root directory
java -cp bin ca.g26final.App
```

### Expected Output
- Swing GUI window opens with 4 tabs: Users, Events, Bookings, Waitlist
- All functionality accessible through GUI buttons

---

## 🔮 Missing Features & Future Phases

### Phase 2: Search & Filter Functionality
- **User Search**: Find users by name, email, or ID
- **Event Search**: Find events by title, date range, location, or type
- **Booking Search**: Find bookings by user, event, status, or date
- **Advanced Filters**: Combine multiple criteria
- **GUI Enhancement**: Add search fields and filter dropdowns to each tab

### Phase 3: Data Persistence
- **File I/O**: Save/load data to/from JSON or CSV files
- **Database Integration**: Replace ArrayList with proper database (SQLite, PostgreSQL)
- **Data Migration**: Handle schema changes and data integrity
- **Backup/Restore**: Export/import functionality

### Phase 4: Automated Testing
- **Unit Tests**: JUnit tests for all model classes and business logic
- **Integration Tests**: Test service layer interactions
- **GUI Tests**: Automated testing of Swing components
- **Test Data**: Sample data generation for testing scenarios

### Phase 5: Advanced Features
- **User Authentication**: Login system with roles (Admin, User)
- **Email Notifications**: Send booking confirmations and reminders
- **Reporting**: Generate reports on usage, popular events, etc.
- **REST API**: Expose functionality via HTTP endpoints
- **Web Interface**: Replace Swing with web-based UI

### Technical Debt & Improvements
- **Error Handling**: More robust exception handling and user feedback
- **Input Validation**: Enhanced validation with better error messages
- **Code Organization**: Extract interfaces, use dependency injection
- **Performance**: Optimize for larger datasets (use Maps for lookups)
- **Logging**: Add proper logging framework
- **Configuration**: External configuration files for settings

---

## 🧪 Demo Scenario

To demonstrate the waitlist promotion functionality:

1. **Create Event**: Workshop with capacity 1
2. **Create Users**: User A (Student), User B (Student)
3. **Book User A**: Gets CONFIRMED status
4. **Book User B**: Gets WAITLISTED status (capacity reached)
5. **Cancel User A's Booking**: Triggers automatic promotion
6. **Result**: User B's booking changes from WAITLISTED to CONFIRMED

This sequence showcases the core business logic and automatic waitlist management.

---

## 📊 Project Metrics

- **Total Classes**: 15 Java files
- **Lines of Code**: ~1500+ lines
- **Packages**: 4 main packages (app, model, service, ui)
- **Inheritance Hierarchies**: 2 main hierarchies (User → 3 subclasses, Event → 3 subclasses)
- **Enums**: 2 status enums (EventStatus, BookingStatus)
- **GUI Components**: 4 tabbed panels with full CRUD operations

---

_Want to explore further? Check out the `src/` directory to examine the code, or run `App.java` to see the application in action. The codebase demonstrates solid OOP principles with proper separation of concerns and comprehensive business logic implementation._


