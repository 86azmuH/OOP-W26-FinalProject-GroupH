package ca.g26final.service;

import  ca.g26final.model.users.User;

import java.util.ArrayList;

public class UserService {

    //Stores all users currently registed in the system
    private ArrayList<User> users;

    public UserService() {
        users = new ArrayList<>();
    }

    //Adds a user to the array list if it is valid and userID is unique
    public boolean addUser(User user) {
        if (user == null) {
            System.out.println("[UserService] addUser failed: user is null");
            return false;
        }

        //makes sure that user ids are not blank
        if (user.getUserId() == null || user.getUserId().isBlank()) {
            System.out.println("[UserService] addUser failed: userId is blank");
            return false;
        }

        //makes sure that the user IDs are different
        if (getUserById(user.getUserId()) != null) {
            System.out.println("[UserService] addUser failed: duplicate userId " + user.getUserId());
            return false;
        }

        users.add(user);
        return true;
    }

    //returns a user with a given userID
    public User getUserById(String userId) {
        if (userId == null || userId.isBlank()) return null;

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
}
