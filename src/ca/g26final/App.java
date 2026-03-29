package ca.g26final;

import ca.g26final.service.BookingService;
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;
import ca.g26final.ui.MainWindow;
import javax.swing.SwingUtilities;

public class App {
    // If an error occurs, throws exception passing it on so it is not handled in main.
    public static void main(String[] args) throws Exception {

        // Initialize the back-end service methods for logic control.
        UserService userService = new UserService();
        EventService eventService = new EventService();
        BookingService bookingService = new BookingService(userService, eventService);

        // Load persisted data (order matters: users -> events -> bookings)
        try {
            userService.loadFromCsv();
            eventService.loadFromCsv();
            bookingService.loadFromCsv();
        } catch (Exception ex) {
            System.out.println("[App] Warning: Failed to load persisted data: " + ex.getMessage());
        }

        // This tells the GUI to run the code on the correct Thread as swing is not thread safe.
        // Therefore, we must tell it to run on the EDT Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            // Pass through the Service logic to the Main Window and run.
            MainWindow window = new MainWindow(userService, eventService, bookingService);
            window.setVisible(true);
        });
    }
}
