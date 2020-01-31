package com.pollapp.service;

import com.pollapp.controller.UserController;
import com.pollapp.dto.request.LoginDTO;
import com.pollapp.entity.Option;
import com.pollapp.entity.Token;
import com.pollapp.entity.User;
import com.pollapp.repository.TokenRepository;
import com.pollapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    static Logger log = java.util.logging.Logger.getLogger(UserController.class.getName());

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);

    }

    public boolean validateUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if(user == null)
            return false;

        if(user.getPassword().equals(loginDTO.getPassword()))
            return true;
        return false;
    }

    public String getUsername(String email) {
        return userRepository.findByEmail(email).getFirstName();
    }

    public String getToken(LoginDTO loginDTO) {

        User user = userRepository.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if(user == null)
            return null;

        Optional<Token> optionalToken = tokenRepository.findById(user.getId());
        Token token;
        if(optionalToken.isEmpty()) {

            token = new Token();
            token.setToken(UUID.randomUUID().toString());
            user.setToken(token);
            token.setUser(user);
            userRepository.save(user);
        }
        else {
            token = optionalToken.get();
            token.setToken(UUID.randomUUID().toString());
            tokenRepository.save(token);
        }

         return token.getToken();
    }

    public User getUserByToken(String token) {
        User user = userRepository.findByTokenToken(token);

        if(user == null)
            return null;

        return user;
    }

    public void invalidateToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            return null;

        User user = optionalUser.get();
        return user;
    }


}
