package ca.g26final.ui;

//Logic
import ca.g26final.service.BookingService;
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;

//Objects
import ca.g26final.model.bookings.Booking;
import ca.g26final.model.events.Event;
import ca.g26final.model.users.Guest;
import ca.g26final.model.users.Staff;
import ca.g26final.model.users.Student;
import ca.g26final.model.users.User;

//Swing Gui tools
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainWindow extends JFrame {

    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;

    //Instance variables that store reference to swing text areas
    //Display multiple lines of text
    private JTextArea usersTextArea;
    private JTextArea eventsTextArea;
    private JTextArea bookingsTextArea;

    //Constructor
    public MainWindow(UserService userService, EventService eventService, BookingService bookingService) {
        this.userService = userService;
        this.eventService = eventService;
        this.bookingService = bookingService;

        //Title on top of window
        setTitle("Campus Event Booking System");
        //Size of window
        setSize(600, 400);
        //Close window when hitting "X"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
