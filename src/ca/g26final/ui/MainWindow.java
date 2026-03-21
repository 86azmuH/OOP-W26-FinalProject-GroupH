package ca.g26final.ui;
// <editor-fold desc = "=== IMPORTS ===">

// ===================================== IMPORTS ================================================
import ca.g26final.model.bookings.Booking; //Model imports
import ca.g26final.model.events.Event; 
import ca.g26final.model.users.Guest;
import ca.g26final.model.users.Staff;
import ca.g26final.model.users.Student;
import ca.g26final.model.users.User;
import ca.g26final.service.BookingService; //Service imports
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;
import java.awt.*; //Standard Library imports
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*; //Swing UI Import
import java.time.format.DateTimeFormatter;

// </editor-fold>

//Creating MainWindow Class as an extension of JFrame(Top level window container)
public class MainWindow extends JFrame {

    // ===================================== FIELDS  =====================================================
    //Service Fields - allows buttons to call service functions etc
    private UserService userService;
    private EventService eventService;
    private BookingService bookingService;

    //UI text areas that display current users, events, and bookings
    //Must be fields as they are updated whenever the state chhanges
    private JTextArea usersTextArea;
    private JTextArea eventsTextArea;
    private JTextArea bookingsTextArea;
    private JTextArea waitlistTextArea;

    //  // ===================================== MAIN WINDOW CONSTRUCTOR  =================================
    public MainWindow(UserService userService, EventService eventService, BookingService bookingService) {
        //setting the fields
        this.userService = userService;
        this.eventService = eventService;
        this.bookingService = bookingService;

        //Giving the frame a title and setting size,height and exit on close
        setTitle("Campus Event Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Creating a tabbedPane - allows us to have tabs for each service
        JTabbedPane tabbedPane = new JTabbedPane();

        //Adding all our tabs to the tabbedPane
        //Users Tab - Given title and called using createUsersPanel method
        tabbedPane.addTab("Users", createUsersPanel());

        //Events Tab
        tabbedPane.addTab("Events", createEventsPanel());

        //Bookings Tab
        tabbedPane.addTab("Bookings", createBookingsPanel());

        //Waitlist Tab
        tabbedPane.addTab("Waitlist", createWaitlistPanel());

        //adding the tabbedPane to the MainWindow Frame - JFrame uses BorderLayout by default
        add(tabbedPane);
        //Adds to Center region, equivelent to add(tabbedPane, BorderLayout.CENTER);
    }
    // <editor-fold desc = "=== PANELS ===">
    // ===================================== CREATING PANEL METHODS  =====================================================
    //Method that creates the Users Panel - returns a JPanel
    //Only used inside MainWindow -> Private
    private JPanel createUsersPanel() {
        //Creating the panel wth a BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        //Adds Padding
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Giving it a title Label
        JLabel label = new JLabel("Users Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        //Adding the title label to the North region
        panel.add(label, BorderLayout.NORTH);

        //Creating a text Area to hold users
        usersTextArea = new JTextArea();
        //Making it un-editable
        usersTextArea.setEditable(false);
        //Creating a ScrollPane for textArea
        JScrollPane scrollPane = new JScrollPane(usersTextArea);
        //adding the scrollPane containing the TextArea to the center region of the users panel
        panel.add(scrollPane, BorderLayout.CENTER);

        //ADD USER
        //Creating a panel for the button
        JPanel buttonPanel = new JPanel();
        //Creating a button to add Users
        JButton addButton = new JButton("Add User");
        //Adding an ActionListener to the button allowing the method addUser() to be executed on press
        addButton.addActionListener(e -> addUser());
        //adding the JButton to the buttonPanel
        buttonPanel.add(addButton);

        //REMOVE USER
        JButton removeButton = new JButton("Remove User");
        removeButton.addActionListener(e -> removeUser());
        buttonPanel.add(removeButton);

        // VIEW USER DETAILS
        JButton detailsButton = new JButton("View User Details");
        detailsButton.addActionListener(e -> viewUserDetails());
        buttonPanel.add(detailsButton);

        //Adds panel to display buttons at south region
        panel.add(buttonPanel, BorderLayout.SOUTH);


        //Refreshes the Users textArea
        refreshUsers();
        //Call refreshUsers to display nousers for now 
        //and for When we add persistence, we want it to display the data from files
        return panel;
    }

    // Create Panel for Events similar to User
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
        // ADD EVENT
        JButton addButton = new JButton("Add Event");
        addButton.addActionListener(e -> addEvent());
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // REMOVE EVENT
        JButton removeButton = new JButton("Cancel Event");
        removeButton.addActionListener(e -> cancelEvent());
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshEvents();
        return panel;
    }

    // Create Panel for Bookings similar to User
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

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(e -> cancelBooking());
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        refreshBookings();
        return panel;
    }

    // Waitlist Panel
    private JPanel createWaitlistPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel label = new JLabel("Waitlist Management");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        waitlistTextArea = new JTextArea();
        waitlistTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(waitlistTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton viewButton = new JButton("View Event Waitlist");
        viewButton.addActionListener(e -> viewWaitlist());
        buttonPanel.add(viewButton);

        JButton removeButton = new JButton("Remove Waitlisted Booking");
        removeButton.addActionListener(e-> removeWaitlist());
        buttonPanel.add(removeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        waitlistTextArea.setText("Select an event to view the waitlist.");
        return panel;
    }

    // </editor-fold>
    // <editor-fold desc = "=== REFRESH ===">
    // ===================================== REFRESH METHODS  =====================================================
    //refresh helpers - functions to refresh the textAreas if changes are made.
    //If the data is changed in any of the service objects, need to update whats displayed in the textAreas

    private void refreshUsers() {
        //Creates List of Users and pulls them from userService object
        List<User> users = userService.getAllUsers();
        //Sets text to no users if the list of users is empty
        if (users.isEmpty()) {
            usersTextArea.setText("<no users>");
        } else {
            //StringBuilder is used to build one big string efficiently instead of doing text+= repeatedly
            //creats StringBuilder object
            StringBuilder sb = new StringBuilder();
            //Iterates through the list and adds the data from each user to the StringBuilder using the toString, 
            //also adds a \n character to display each user line by line
            for (User u : users) {
                sb.append(u.toString()).append("\n");
            }
            //Sets the text in the TextArea to the stringBuilder using the toString
            usersTextArea.setText(sb.toString());
        }
    }

    //Same thing for events
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

    //Same thing for bookings
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

    // </editor-fold>
    // <editor-fold desc = "=== BUTTON ACTIONS ===">
    // ===================================== BUTTON ACTIONS =====================================================
    // Actions triggered by buttons
    // Add User Button
    private void addUser() {
        //OptionPane for each input - popup that pauses program and allows input
        //showInputDialog - creates a modal dialog prompts user to enter a value and returns what user typed
        //Returns String input and null if cancel is pressed
        String id = JOptionPane.showInputDialog(this, "User ID:");
        //if cancel was pressed or input was left blank, exits addUser method
        if (id == null || id.isBlank())
            return;
        String name = JOptionPane.showInputDialog(this, "Name:");
        if (name == null)
            return;
        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null)
            return;

        //This Section uses an overloaded version of JOptionPane.showInputDialog
        //It replaces the normal text field with a dropdown list.
        //Lets user choose from predefined options
        //List of options
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

        //Initialize the correct constructor based on input
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
        // Run back-end logic to validate adding user to the ArrayList.
        boolean ok = userService.addUser(user);
        if (ok)
            JOptionPane.showMessageDialog(this, "User added");
        else
            JOptionPane.showMessageDialog(this, "Failed to add user (check console for details)");
        refreshUsers();
    }
    // Add Event Button
    private void addEvent() {
        // Input Prompts that are stored to the respective variables.
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

        // Run back-end logic to see if Event was added successfully.
        Event ev = new Event(id, title, dt, location, cap);
        boolean ok = eventService.addEvent(ev);
        if (ok)
            JOptionPane.showMessageDialog(this, "Event added");
        else
            JOptionPane.showMessageDialog(this, "Failed to add event (check console)");
        refreshEvents();
    }

    private void bookEvent() {
        // Input Prompts that are stored to the respective variables.
        String userId = JOptionPane.showInputDialog(this, "User ID:");
        if (userId == null || userId.isBlank())
            return;
        String eventId = JOptionPane.showInputDialog(this, "Event ID:");
        if (eventId == null || eventId.isBlank())
            return;

        // Back-end Validation to see if booking was successful.
        Booking booking = bookingService.bookEvent(userId, eventId);
        if (booking != null) {
            JOptionPane.showMessageDialog(this, "Booking created: " + booking.getBookingId());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking (see console)");
        }
        refreshBookings();
    }

    // View User Details
    private void viewUserDetails() {
        String userId = JOptionPane.showInputDialog(this, "Enter User ID:");
        if (userId == null || userId.isBlank()) {
            return;
        }

        // Searches for the user and sets foundUser to the user of interest
        User foundUser = null;
        for (User u : userService.getAllUsers()) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                foundUser = u;
                break;
            }
        }

        // If nothing is set to foundUser then it could not locate it in the previous loop.
        if (foundUser == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        // Building the display for the found user
        StringBuilder sb = new StringBuilder();
        sb.append("User Details\n");
        sb.append("-------------------------\n");
        sb.append("User ID: ").append(foundUser.getUserId()).append("\n");
        sb.append("Name: ").append(foundUser.getName()).append("\n");
        sb.append("Email: ").append(foundUser.getEmail()).append("\n");
        sb.append("Type: ").append(foundUser.getClass().getSimpleName()).append("\n\n");
        // Get simple name converts into a clean string

        sb.append("Bookings Summary\n");
        sb.append("-------------------------\n");

        // Checks for all bookings associated with user and adds to our string builder.
        int count = 0;
        for (Booking b : bookingService.getAllBookings()) {
            if (b.getUserId().equalsIgnoreCase(userId)) {
                sb.append("Booking ID: ").append(b.getBookingId()).append("\n");
                sb.append("Event ID: ").append(b.getEventId()).append("\n");
                sb.append("Status: ").append(b.getBookingStatus()).append("\n");
                sb.append("Created At: ").append(b.getCreatedAt().format(formatter)).append("\n");
                sb.append("-------------------------\n");
                count++;
            }
        }
        if (count == 0) {
            sb.append("No bookings found for this user.\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // Waitlist Buttons
    private void viewWaitlist(){
        String eventId = JOptionPane.showInputDialog(this, "Enter Event ID:");
        if(eventId == null || eventId.isBlank()){
            return;
        }

        List<Booking> waitlist = bookingService.getWaitlist(eventId);

        if(waitlist.isEmpty()) {
            waitlistTextArea.setText("No waitlisted bookings for event" + eventId);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Waitlist for Event ").append(eventId).append("\n\n");

        for (Booking b : waitlist) {
            sb.append(b.toString()).append("\n");
        }

        waitlistTextArea.setText(sb.toString());
    }

    private void removeWaitlist(){
        String bookingId = JOptionPane.showInputDialog(this,
                "Enter Booking ID to remove from waitlist: ");
        if(bookingId == null || bookingId.isBlank()){
            return;
        }

        boolean ok = bookingService.removeWaitlistedBooking(bookingId);

        if(ok){
            JOptionPane.showMessageDialog(this, "Wailisted booking removed");
            refreshBookings();
            waitlistTextArea.setText("Waitlisted booking " + bookingId + " was removed.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to " +
                    "remove waitlisted booking");
        }
    }



    // </editor-fold>
    // <editor-fold desc = "=== REMOVE + CANCEL ===">

    //==================================== REMOVE / CANCEL METHODS =============================================

    private void removeUser(){
        String id = JOptionPane.showInputDialog(this, "Enter User ID to remove:");
        if (id == null || id.isBlank()){
            return;
        }

        if (bookingService.hasBookingsForUser(id)){
            JOptionPane.showMessageDialog(this, "Cannot remove user: this user has existing bookings");
            return;
        }
        boolean ok = userService.removeUser(id);
        if(ok){
            JOptionPane.showMessageDialog(this, "User removed");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to remove user (user may not exist)");
        }
        refreshUsers();
    }
    // CANCEL EVENT
    private void cancelEvent(){
        String id = JOptionPane.showInputDialog(this, "Enter Event ID to cancel:");
        if(id == null || id.isBlank()){
            return;
        }
        boolean ok = eventService.cancelEvent(id);
        if(ok){
            int changed = bookingService.cancelBookingsForEvent(id);
            JOptionPane.showMessageDialog(this, "Event cancelled. \nBookings cancelled: " + changed);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to cancel event (event may not exist)");
        }
        refreshEvents();
        refreshBookings();
    }
    // CANCEL BOOKING
    private void cancelBooking(){
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to cancel");
        if(bookingId == null || bookingId.isBlank()){
            return;
        }

        boolean ok = bookingService.cancelBooking(bookingId);

        if(ok){
            JOptionPane.showMessageDialog(this, "Booking cancelled");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to cancel booking (booking may not exist)");
        }
        refreshBookings();
    }
    //</editor-fold>
}
