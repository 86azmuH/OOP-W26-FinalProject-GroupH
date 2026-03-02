package ca.g26final.ui;

import ca.g26final.service.BookingService;
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;

import ca.g26final.model.bookings.Booking;
import ca.g26final.model.events.Event;
import ca.g26final.model.users.Guest;
import ca.g26final.model.users.Staff;
import ca.g26final.model.users.Student;
import ca.g26final.model.users.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainWindow extends JFrame {
    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;

    // text areas so we can refresh their contents later
    private JTextArea usersTextArea;
    private JTextArea eventsTextArea;
    private JTextArea bookingsTextArea;

    public MainWindow(UserService userService, EventService eventService, BookingService bookingService) {
        this.userService = userService;
        this.eventService = eventService;
        this.bookingService = bookingService;

        setTitle("Campus Event Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Users Tab
        tabbedPane.addTab("Users", createUsersPanel());

        // Events Tab
        tabbedPane.addTab("Events", createEventsPanel());

        // Bookings Tab
        tabbedPane.addTab("Bookings", createBookingsPanel());

        add(tabbedPane);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Users Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        usersTextArea = new JTextArea();
        usersTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(usersTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshUsers();
        return panel;
    }

    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Events Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        eventsTextArea = new JTextArea();
        eventsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Event");
        addButton.addActionListener(e -> addEvent());
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshEvents();
        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Bookings Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        bookingsTextArea = new JTextArea();
        bookingsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton bookButton = new JButton("Book Event");
        bookButton.addActionListener(e -> bookEvent());
        buttonPanel.add(bookButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshBookings();
        return panel;
    }

    // refresh helpers
    private void refreshUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            usersTextArea.setText("<no users>");
        } else {
            StringBuilder sb = new StringBuilder();
            for (User u : users) {
                sb.append(u.toString()).append("\n");
            }
            usersTextArea.setText(sb.toString());
        }
    }

    private void refreshEvents() {
        List<Event> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            eventsTextArea.setText("<no events>");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Event ev : events) {
                sb.append(ev.toString()).append("\n");
            }
            eventsTextArea.setText(sb.toString());
        }
    }

    private void refreshBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            bookingsTextArea.setText("<no bookings>");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Booking b : bookings) {
                sb.append(b.toString()).append("\n");
            }
            bookingsTextArea.setText(sb.toString());
        }
    }

    // actions triggered by buttons
    private void addUser() {
        String id = JOptionPane.showInputDialog(this, "User ID:");
        if (id == null || id.isBlank())
            return;
        String name = JOptionPane.showInputDialog(this, "Name:");
        if (name == null)
            return;
        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null)
            return;

        String[] types = { "Student", "Staff", "Guest" };
        String type = (String) JOptionPane.showInputDialog(
                this,
                "Type:",
                "Select user type",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]);
        if (type == null)
            return;

        User user;
        switch (type) {
            case "Student":
                user = new Student(id, name, email);
                break;
            case "Staff":
                user = new Staff(id, name, email);
                break;
            default:
                user = new Guest(id, name, email);
                break;
        }

        boolean ok = userService.addUser(user);
        if (ok)
            JOptionPane.showMessageDialog(this, "User added");
        else
            JOptionPane.showMessageDialog(this, "Failed to add user (check console for details)");
        refreshUsers();
    }

    private void addEvent() {
        String id = JOptionPane.showInputDialog(this, "Event ID:");
        if (id == null || id.isBlank())
            return;
        String title = JOptionPane.showInputDialog(this, "Title:");
        if (title == null)
            return;
        String dtStr = JOptionPane.showInputDialog(this, "Date/time (YYYY-MM-DDTHH:MM):");
        if (dtStr == null)
            return;
        LocalDateTime dt;
        try {
            dt = LocalDateTime.parse(dtStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date time format");
            return;
        }
        String location = JOptionPane.showInputDialog(this, "Location:");
        if (location == null)
            return;
        String capStr = JOptionPane.showInputDialog(this, "Capacity:");
        if (capStr == null)
            return;
        int cap;
        try {
            cap = Integer.parseInt(capStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number");
            return;
        }

        Event ev = new Event(id, title, dt, location, cap);
        boolean ok = eventService.addEvent(ev);
        if (ok)
            JOptionPane.showMessageDialog(this, "Event added");
        else
            JOptionPane.showMessageDialog(this, "Failed to add event (check console)");
        refreshEvents();
    }

    private void bookEvent() {
        String userId = JOptionPane.showInputDialog(this, "User ID:");
        if (userId == null || userId.isBlank())
            return;
        String eventId = JOptionPane.showInputDialog(this, "Event ID:");
        if (eventId == null || eventId.isBlank())
            return;

        Booking booking = bookingService.bookEvent(userId, eventId);
        if (booking != null) {
            JOptionPane.showMessageDialog(this, "Booking created: " + booking.getBookingId());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking (see console)");
        }
        refreshBookings();
    }

}
