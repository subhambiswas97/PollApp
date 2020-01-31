package com.pollapp.controller;

import com.pollapp.dto.request.LoginDTO;
import com.pollapp.dto.request.RegisterDTO;
import com.pollapp.dto.request.TokenDTO;
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
    @CrossOrigin
    public List<User> getUsers() {
        log.info("getUsers");
        return userService.getUsers();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<Object> addUser(@RequestBody RegisterDTO registerDTO) {
        log.info("Post hitted");
        System.out.println(registerDTO.getEmail());
        System.out.println(registerDTO.getFirstName());

        ResponseEntity<Object> responseEntity;
        try {
            UserValidator.validateUser(registerDTO);
            User user = new User();
            user.setEmail(registerDTO.getEmail());
            user.setFirstName(registerDTO.getFirstName());
            user.setLastName(registerDTO.getLastName());
            user.setPassword(registerDTO.getPassword());
            userService.addUser(user);
            responseEntity = new ResponseEntity<>("OK",HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            responseEntity = new ResponseEntity<>(e.toString(),HttpStatus.valueOf(401));
        }
        return responseEntity;
    }

    @PostMapping(value = "/loginvalidate")
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


    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity<Object> getToken(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO.getEmail());
        System.out.println(loginDTO.getPassword());

        ResponseEntity<Object> responseEntity;

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
            log.info(e.toString());
            responseEntity = new ResponseEntity<>(e.toString(),HttpStatus.valueOf(401));
        }

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getuser")
    public ResponseEntity<Object> getUser(@RequestBody TokenDTO tokenDTO) {
        try {
            System.out.println(tokenDTO.getUserToken());
            User user = userService.getUserByToken(tokenDTO.getUserToken());
            if(user == null)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @CrossOrigin
    public ResponseEntity<Object> logout(@RequestBody TokenDTO tokenDTO) {
        try {
            UserValidator.validateUserToken(tokenDTO.getUserToken());
            userService.invalidateToken(tokenDTO.getUserToken());
            return new ResponseEntity<>(tokenDTO.getUserToken(),HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }



}
