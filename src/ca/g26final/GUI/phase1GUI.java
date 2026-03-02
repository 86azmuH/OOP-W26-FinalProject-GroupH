package ca.g26final.gui;

import ca.g26final.model.bookings.Booking;
import ca.g26final.model.events.*;
import ca.g26final.model.users.*;
import ca.g26final.service.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Phase1GUI {

    // Services
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();
    private final BookingService bookingService = new BookingService(userService, eventService);

    // GUI lists
    private final ObservableList<User> usersObs = FXCollections.observableArrayList();
    private final ObservableList<Event> eventsObs = FXCollections.observableArrayList();
    private final ObservableList<Booking> bookingsObs = FXCollections.observableArrayList();

    // shared status label
    private final Label status = new Label("");

    // bookings controls
    private final ComboBox<User> userBox = new ComboBox<>();
    private final ComboBox<Event> eventBox = new ComboBox<>();
    private final TableView<Booking> bookingTable = new TableView<>();

    public Phase1GUI() {
        seedDemoDataIntoServices(); // loads your real services (can replace later)
        syncObsFromServices();      // copies service lists into ObservableLists
        buildUI();
        refreshBookings();
    }

    // --------------- UI BUILD -----------
    private void buildUI() {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(makeUsersTab());
        tabs.getTabs().add(makeEventsTab());
        tabs.getTabs().add(makeBookingsTab());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        status.setPadding(new Insets(10));
        status.setWrapText(true);
        status.setText("Ready.");

        setCenter(tabs);
        setBottom(status);
    }
}
