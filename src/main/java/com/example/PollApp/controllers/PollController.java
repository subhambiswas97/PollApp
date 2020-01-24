package com.example.PollApp.controllers;

import com.example.PollApp.exceptions.BadRequestException;
import com.example.PollApp.models.LoginCredentials;
import com.example.PollApp.models.Response;
import com.example.PollApp.models.TokenWrapper;
import com.example.PollApp.models.User;
import com.example.PollApp.services.UserService;
import com.example.PollApp.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class PollController {

    //@Autowired
    //private PollRepository pollRepository;

    //@Autowired
    //private UserRepository userRepository;

    @Autowired
    private UserService userService;

    static Logger log = Logger.getLogger(PollController.class.getName());

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<User> getUsers() {
        log.info("Get hitted");
        //return pollRepository.getUsers();
        return userService.getUsers();
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    //@CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        log.info("Post hitted");
        System.out.println(user.getEmail());
        System.out.println(user.getFirstname());
        //pollRepository.addUser(user);

        ResponseEntity<String> responseEntity;
        try {
            userValidator.validateUser(user);
            userService.addUser(user);
            responseEntity = new ResponseEntity<>("OK",HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.toString(),HttpStatus.valueOf(401));
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginvalidate")
    public String validateLogin(@RequestBody LoginCredentials loginCredentials) {
        System.out.println(loginCredentials.getEmail());
        System.out.println(loginCredentials.getPassword());
        if(userService.validateUser(loginCredentials)) {
            String username = userService.getUsername(loginCredentials.getEmail());
            if(username==null)
                return "Faliure";
            return username;
        }
        return "Faliure";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/gettoken")
    public ResponseEntity<String> getToken(@RequestBody LoginCredentials loginCredentials) {
        System.out.println(loginCredentials.getEmail());
        System.out.println(loginCredentials.getPassword());

        //Response response = new Response();
        ResponseEntity<String> responseEntity;
        try {
            userValidator.validateLogin(loginCredentials);
            //log.info("***request token 01***");
            String token = userService.getToken(loginCredentials);
            //log.info("***request token 02***");
            if(token == null) {
                //log.info("***request token==null 01***");
                responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            } else {
                //log.info("***request token!=null 01***");
                responseEntity = new ResponseEntity<>(token, HttpStatus.OK);
            }

        } catch(Exception e) {
            //log.info("***request token exception 01***");
            responseEntity = new ResponseEntity<>(e.toString(),HttpStatus.valueOf(401));
        }


        /*else {
            response.setStatus(200);
            response.setToke
        }
        return response;
         */
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getuser")
    public ResponseEntity<User> getUser(@RequestBody TokenWrapper tokenWrapper) {
        System.out.println(tokenWrapper.getToken());
        return userService.getUserByToken(tokenWrapper.getToken());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public ResponseEntity<String> logout(@RequestBody TokenWrapper tokenWrapper) {
        userService.deleteToken(tokenWrapper.getToken());
        return new ResponseEntity<>(tokenWrapper.getToken(),HttpStatus.OK);
    }



}
