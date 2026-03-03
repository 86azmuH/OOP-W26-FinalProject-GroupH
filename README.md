# Campus Event Booking System

This repository contains a simple Java desktop application for managing
campus events, users, and bookings. The focus of **Phase 1** is the core
logic and a basic Swing GUI. During our demo we'll walk through each
layer of the project and point out the main responsibilities.

Reminder:
- Our Project Follows an MVC Architechture Pattern(Model, View, Controller)
- Persistence was not done yet as it was not a part of Phase 1's requirements, So we havent implemented it yet
- Theres a lack of branching in our git, this is because we started this project without much knowledge on git -> We plan to implement branches as we work on Phase 2


---

## 🧩 Project Overview

An administrator can:

1. Create users (Student, Staff, Guest)
2. Create events (Workshop, Seminar, Concert)
3. Book users into events
4. Cancel bookings
5. Waitlist users when an event is full
6. Automatically promote from the waitlist when a confirmed booking is
   cancelled

> ⚠️ Phase 1 intentionally omits search/filter, persistence, and
> automated tests – those are planned for later phases.

---

## 📁 Key Packages & Folders

The `src/ca/g26final` package is split into:

* `model` – domain objects (users, events, bookings)
* `service` – application logic and storage
* `ui` – Swing-based graphical interface

```
src/ca/g26final
├── model
│   ├── bookings
│   ├── events
│   └── users
├── service
│   ├── BookingService.java
│   ├── EventService.java
│   └── UserService.java
└── ui
    └── MainWindow.java
```

> The `bin` folder contains compiled classes produced by the build.

---

## 🔧 Data Storage (BookingService)

All data lives in `BookingService` using simple `ArrayList` collections:
`users`, `events`, and `bookings`. No maps or repository patterns are
used; this keeps the implementation straightforward for Phase 1.

---

## 🧠 Domain Model

### 1. User hierarchy

* `User` (abstract)
  * Fields: `userId`, `name`, `email`
  * Method: `getMaxConfirmedBookings()` – overridden by subclasses
* `Student` – allows 3 active bookings
* `Staff` – allows 5 active bookings
* `Guest` – allows 1 active booking

### 2. Event hierarchy

* `Event` (abstract)
  * Fields: `eventId`, `title`, `dateTime`, `location`, `capacity`,
    `status`
  * Methods: getters, `cancel()`, `isActive()`
* Subclasses add type‑specific data:
  * `Workshop` → `topic`
  * `Seminar` → `speakerName`
  * `Concert` → `ageRestriction` (display only)

### 3. Booking class

* Fields: `bookingId`, `userId`, `eventId`, `createdAt`, `status`
* `BookingStatus`: `CONFIRMED`, `WAITLISTED`, `CANCELLED`

>User and event IDs link bookings to their parent objects.

---

## 🔁 Booking Rules (Core Logic)

1. User and event must exist & event must be **ACTIVE**
2. No duplicate bookings for the same user/event
3. Respect per‑user confirmed booking limits
4. If event capacity isn’t reached → `CONFIRMED`, otherwise
   `WAITLISTED`

### 🛑 Cancellation behavior

* **WAITLISTED** cancellation → status set to `CANCELLED` only
* **CONFIRMED** cancellation → status set to `CANCELLED` and the
  earliest waitlisted booking for that event is promoted

### 🧨 Cancelling an event

When an event is cancelled, all its confirmed and waitlisted bookings
are marked `CANCELLED` and the event’s status flips to `CANCELLED`.

---

## 🛠 Service Layer Responsibilities

* **BookingService** – holds lists and implements all operations:
  adding users/events, booking, cancelling, and retrieval helpers.
* **UserService** – validates and constructs `User` instances.
* **EventService** – validates and constructs `Event` instances.

Both `UserService` and `EventService` delegate to `BookingService` after
their checks.

---

## 🖥 GUI Walk‑through (MainWindow)

The Swing-based UI provides buttons for each required action: add/list
users, add/list events, book/cancel, and view event rosters. During the
demo we’ll step through each screen and show how it ties back to the
service layer.

---

## 🎯 Demo Scenario

To highlight the waitlist promotion logic, we’ll perform the following
actions:

1. Create an event with capacity 1
2. Book **User A** → `CONFIRMED`
3. Book **User B** → `WAITLISTED`
4. Cancel **User A**’s booking
5. Observe **User B** automatically become `CONFIRMED`

This simple sequence demonstrates the core functionality of the system
and will be the backbone of our presentation.

---

_Want to explore further? Take a look at `src/` to read the code or run
`App` from the command line._

Good luck with your demo! 🚀
