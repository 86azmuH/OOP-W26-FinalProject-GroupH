package ca.g26final.service;

import ca.g26final.model.users.Guest;
import ca.g26final.model.users.Staff;
import ca.g26final.model.users.Student;
import ca.g26final.model.users.User;
import ca.g26final.persistence.CsvUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    // Stores all users currently registed in the system
    private ArrayList<User> users;
    private final Path usersCsvPath;

    public UserService() {
        this(CsvUtil.resolveDataPath("users.csv"));
    }

    public UserService(Path csvPath) {
        users = new ArrayList<>();
        this.usersCsvPath = csvPath;
    }

    // Adds a user to the array list if it is valid and userID is unique
    public boolean addUser(User user) {
        if (user == null) {
            System.out.println("[UserService] addUser failed: user is null");
            return false;
        }

        // makes sure that user ids are not blank
        if (user.getUserId() == null || user.getUserId().isBlank()) {
            System.out.println("[UserService] addUser failed: userId is blank");
            return false;
        }

        // makes sure that the user IDs are different
        if (getUserById(user.getUserId()) != null) {
            System.out.println("[UserService] addUser failed: duplicate userId " + user.getUserId());
            return false;
        }

        users.add(user);
        // Persist on successful mutation
        try {
            updateFile();
        } catch (Exception ignored) {
        }
        return true;
    }

    // returns a user with a given userID
    public User getUserById(String userId) {
        if (userId == null || userId.isBlank())
            return null;

        for (User u : users) {
            if (u.getUserId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    // Returns all users (for listing in UI)
    public ArrayList<User> getAllUsers() {
        return users;
    }

    // True if user exists
    public boolean userExists(String userId) {
        return getUserById(userId) != null;
    }

    // Removes a user with the given userId
    public boolean removeUser(String userId) {
        if (userId == null || userId.isBlank()) {
            System.out.println("[UserService] removeUser failed: userId is blank.");
            return false;
        }
        User user = getUserById(userId);
        if (user == null) {
            System.out.println("[UserService] removeUser failed: user not found.");
            return false;
        }

        users.remove(user);
        try {
            updateFile();
        } catch (Exception ignored) {
        }
        return true;
    }

    // Persistence: load users from CSV
    // Preferred format: userId,name,email,userType
    // Legacy format supported: userId,type,name,email
    public void loadFromCsv() throws Exception {
        List<String> lines = CsvUtil.readAll(usersCsvPath);
        users.clear();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 4)
                continue;

            // Skip header rows
            if (parts[0].trim().equalsIgnoreCase("userId")) {
                continue;
            }

            String id = parts[0].trim();
            String name;
            String email;
            String type;

            // Detect preferred schema (userId,name,email,userType)
            if (isKnownUserType(parts[3].trim())) {
                name = parts[1].trim();
                email = parts[2].trim();
                type = parts[3].trim().toUpperCase();
            } else {
                // Legacy fallback (userId,type,name,email)
                type = parts[1].trim().toUpperCase();
                name = parts[2].trim();
                email = parts[3].trim();
            }

            User u;
            switch (type) {
                case "STUDENT":
                    u = new Student(id, name, email);
                    break;
                case "STAFF":
                    u = new Staff(id, name, email);
                    break;
                case "GUEST":
                    u = new Guest(id, name, email);
                    break;
                default: // fallback to Student if unknown
                    u = new Student(id, name, email);
            }
            users.add(u);
        }
    }

    // Writes current users to CSV
    public void updateFile() throws Exception {
        ArrayList<String> out = new ArrayList<>();

        // Keep CSV output aligned with assignment starter schema.
        out.add("userId,name,email,userType");

        for (User u : users) {
            String type = u.getClass().getSimpleName();
            out.add(String.join(",",
                    safe(u.getUserId()),
                    safe(u.getName()),
                    safe(u.getEmail()),
                    safe(type)));
        }
        CsvUtil.writeAll(usersCsvPath, out);
    }

    private String safe(String v) {
        return v == null ? "" : v.replace(",", " ");
    }

    private boolean isKnownUserType(String value) {
        if (value == null)
            return false;
        String normalized = value.trim().toUpperCase();
        return normalized.equals("STUDENT") || normalized.equals("STAFF") || normalized.equals("GUEST");
    }
}
