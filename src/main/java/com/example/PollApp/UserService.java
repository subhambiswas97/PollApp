package com.example.PollApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void addUser(User user) {
        Token token = new Token();
        token.setToken(UUID.randomUUID().toString());
        user.setToken(token);
        token.setUser(user);
        userRepository.save(user);

    }

    public boolean validateUser(LoginCredentials loginCredentials) {
        User user = userRepository.findByEmail(loginCredentials.getEmail());
        if(user == null)
            return false;
        //String password = userRepository.findPasswordByEmail(loginCredentials.getEmail());

        if(user.getPassword().equals(loginCredentials.getPassword()))
            return true;
        return false;
    }

    public String getUsername(String email) {
        return userRepository.findByEmail(email).getFirstname();
    }

    public String getToken(LoginCredentials loginCredentials) {
        User user = userRepository.findByEmailAndPassword(loginCredentials.getEmail(),loginCredentials.getPassword());
        if(user == null)
            return null;

         return user.getToken().getToken();
    }

    public User getUserByToken(String token) {
        User user = userRepository.findByTokenToken(token);
        System.out.println(user);
        if(user == null)
            return null;
        System.out.println("******"+user.getFirstname()+"***********");
        return user;
    }


}
