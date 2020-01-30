package com.pollapp.service;

import com.pollapp.controller.UserController;
import com.pollapp.dto.requestdto.LoginDTO;
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
        //log.info("***gettoken 01***");
        User user = userRepository.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
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

    public User getUserByToken(String token) {
        User user = userRepository.findByTokenToken(token);
        //System.out.println(user);
        if(user == null)
            return null;
            //return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        //System.out.println("******"+user.getFirstname()+"***********");
        //return new ResponseEntity<>(user,HttpStatus.OK);
        return user;
    }

    public void invalidateToken(String token) {
        tokenRepository.deleteByToken(token);
    }


}
