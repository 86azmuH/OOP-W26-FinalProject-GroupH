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

        // =============== TABS =================================
        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        // Users Tab
        tabbedPane.addTab("Users", createUsersPanel());
        // Events Tab
       // tabbedPane.addTab("Events", createEventsPanel());
        // Bookings Tab
       // tabbedPane.addTab("Bookings", createBookingsPanel());

        add(tabbedPane);
    }
    // USERS PANEL
    // This panel will display users and allow you to manage (add/remove)
    private JPanel createUsersPanel(){
        //Create main panel for Users
        JPanel panel = new JPanel(new BorderLayout());
        // Padding around the edges for better spacing
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //Tab Title
        JLabel label = new JLabel("Users Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        // Text area to display list of users
        usersTextArea = new JTextArea();
        // Prevent manual editing
        usersTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(usersTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        //Add button, when clicked call addUser method
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshUsers();
        return panel;
    }

    // ================== REFRESH HELPERS =========================
    // Synchronizes the UI with latest data from service layer (logic).
    private void refreshUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()){
            usersTextArea.setText("<no users>");
        } else {
            StringBuilder sb =  new StringBuilder();
            for (User u : users) {
                sb.append(u.toString()).append("\n");
            }
            usersTextArea.setText(sb.toString());
        }
    }

    // =================== BUTTON ACTIONS =============================
    private void addUser(){
        //Popups to allow entering user information and saving it.
        //this centers the popup on the window.
        //Each input is validated then stored to be used for the constructor.
        String id = JOptionPane.showInputDialog(this, "User ID:");
        if (id == null || id.isBlank()){ return; }
        String name = JOptionPane.showInputDialog(this, "Name: ");
        if(name == null) { return; }
        String email = JOptionPane.showInputDialog(this, "Email:");
        if(email == null) { return; }
        String[] types = { "Student", "Staff", "Guest" };
        String type = (String) JOptionPane.showInputDialog(this,
                "Type:", "Select user type", JOptionPane.QUESTION_MESSAGE, null, types,
                types[0]);
        if(type == null){ return; }

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
        //Check if user is successfully added
        boolean ok = userService.addUser(user);
        if(ok) {
            JOptionPane.showMessageDialog(this, "User added");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user (check console for details)");
        }
        refreshUsers();
    }

}
