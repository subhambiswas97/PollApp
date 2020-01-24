package com.pollapp.controller;

import com.pollapp.dto.LoginDTO;
import com.pollapp.dto.TokenDTO;
import com.pollapp.entity.User;
import com.pollapp.service.UserService;
import com.pollapp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user")
    public List<User> getUsers() {
        log.info("getUsers");
        return userService.getUsers();
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    //@CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        log.info("Post hitted");
        System.out.println(user.getEmail());
        System.out.println(user.getFirstName());

        ResponseEntity<String> responseEntity;
        try {
            UserValidator.validateUser(user);
            userService.addUser(user);
            responseEntity = new ResponseEntity<>("OK",HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.toString(),HttpStatus.valueOf(401));
        }
        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginvalidate")
    public String validateLogin(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO.getEmail());
        System.out.println(loginDTO.getPassword());
        if(userService.validateUser(loginDTO)) {
            String username = userService.getUsername(loginDTO.getEmail());
            if(username==null)
                return "Faliure";
            return username;
        }
        return "Faliure";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/gettoken")
    public ResponseEntity<String> getToken(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO.getEmail());
        System.out.println(loginDTO.getPassword());

        //Response response = new Response();
        ResponseEntity<String> responseEntity;
        try {
            UserValidator.validateLogin(loginDTO);
            //log.info("***request token 01***");
            String token = userService.getToken(loginDTO);
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
    public ResponseEntity<User> getUser(@RequestBody TokenDTO tokenDTO) {
        System.out.println(tokenDTO.getToken());
        return userService.getUserByToken(tokenDTO.getToken());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDTO tokenDTO) {
        userService.invalidateToken(tokenDTO.getToken());
        return new ResponseEntity<>(tokenDTO.getToken(),HttpStatus.OK);
    }



}
