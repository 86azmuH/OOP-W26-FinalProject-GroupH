package ca.g26final;

import ca.g26final.service.BookingService;
import ca.g26final.service.EventService;
import ca.g26final.service.UserService;
import ca.g26final.ui.MainWindow;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        BookingService bookingService = new BookingService(userService, eventService);
        
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(userService, eventService, bookingService);
            window.setVisible(true);
        });
    }
}
