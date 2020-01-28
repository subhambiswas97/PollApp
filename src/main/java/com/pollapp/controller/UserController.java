package com.pollapp.controller;

import com.pollapp.dto.LoginDTO;
import com.pollapp.dto.RegisterDTO;
import com.pollapp.dto.TokenDTO;
import com.pollapp.entity.User;
import com.pollapp.service.UserService;
import com.pollapp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;



@RestController
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    /*@RequestMapping(value= "/**", method=RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }

     */

//    @RequestMapping(value= "/gettoken")
//    public ResponseEntity corsHeaders() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Access-Control-Allow-Origin", "*");
//        httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        httpHeaders.add("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
//        httpHeaders.add("Access-Control-Max-Age", "3600");
//        return new ResponseEntity(null,httpHeaders,HttpStatus.OK);
//    }



    @GetMapping(value = "/user")
    @CrossOrigin
    public List<User> getUsers() {
        log.info("getUsers");
        return userService.getUsers();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/users")
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


    @PostMapping(value = "/gettoken")
    @CrossOrigin
    public ResponseEntity<Object> getToken(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO.getEmail());
        System.out.println(loginDTO.getPassword());

        //Response response = new Response();
        ResponseEntity<Object> responseEntity;
        //HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add("Access-Control-Allow-Origin","http://localhost:3000/**");

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


        /*else {
            response.setStatus(200);
            response.setToke
        }
        return response;
         */

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getuser")
    public ResponseEntity<Object> getUser(@RequestBody TokenDTO tokenDTO) {
        try {
            System.out.println(tokenDTO.getToken());
            User user = userService.getUserByToken(tokenDTO.getToken());
            if(user == null)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public ResponseEntity<Object> logout(@RequestBody TokenDTO tokenDTO) {
        try {
            UserValidator.validateUserToken(tokenDTO.getToken());
            userService.invalidateToken(tokenDTO.getToken());
            return new ResponseEntity<>(tokenDTO.getToken(),HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }



}
