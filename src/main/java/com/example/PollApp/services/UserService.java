package com.example.PollApp.services;

import com.example.PollApp.controllers.PollController;
import com.example.PollApp.models.LoginCredentials;
import com.example.PollApp.models.Token;
import com.example.PollApp.models.User;
import com.example.PollApp.repositories.TokenRepository;
import com.example.PollApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    static Logger log = java.util.logging.Logger.getLogger(PollController .class.getName());

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);

    }

    public boolean validateUser(LoginCredentials loginCredentials) {
        User user = userRepository.findByEmail(loginCredentials.getEmail());
        if(user == null)
            return false;

        if(user.getPassword().equals(loginCredentials.getPassword()))
            return true;
        return false;
    }

    public String getUsername(String email) {
        return userRepository.findByEmail(email).getFirstname();
    }

    public String getToken(LoginCredentials loginCredentials) {
        //log.info("***gettoken 01***");
        User user = userRepository.findByEmailAndPassword(loginCredentials.getEmail(),loginCredentials.getPassword());
        //log.info("***gettoken 02***");
        if(user == null)
            return null;
        //log.info("***gettoken 03***");
        Optional<Token> optionalToken = tokenRepository.findById(user.getId());
        Token token;
        if(optionalToken.isEmpty()) {
            //log.info("***token=null 01***");
            token = new Token();
            token.setToken(UUID.randomUUID().toString());
            user.setToken(token);
            //log.info("***token=null 02***");
            token.setUser(user);
            userRepository.save(user);
            //log.info("***token=null 03***");
        }
        else {
            token = optionalToken.get();
            //log.info("***token!=null 01***");
            token.setToken(UUID.randomUUID().toString());
            tokenRepository.save(token);
            //log.info("***token!=null 02***");
        }
        //log.info("***return token 01***");
         return token.getToken();
    }

    public ResponseEntity<User> getUserByToken(String token) {
        User user = userRepository.findByTokenToken(token);
        //System.out.println(user);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        //System.out.println("******"+user.getFirstname()+"***********");
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }


}
