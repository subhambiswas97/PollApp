package com.example.PollApp;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PollRepository {

    private List<User> userList = new ArrayList<User>();


    PollRepository() {
        userList.add(new User("subhambiswas@gmail.com","Subham","Biswas","sample"));
    }

    public List<User> getUsers() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }
}
