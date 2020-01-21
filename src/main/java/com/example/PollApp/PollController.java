package com.example.PollApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    static Logger log = Logger.getLogger(PollController.class.getName());

    @RequestMapping(method = RequestMethod.GET, value = "/Users")
    public List<User> getUsers() {
        log.info("Get hitted");
        //return pollRepository.getUsers();
        return (List<User>) userRepository.findAll();
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/Users")
    //@CrossOrigin(origins = "http://localhost:3000")
    public void addUser(@RequestBody User user) {
        log.info("Post hitted");
        System.out.println(user.getEmail());
        System.out.println(user.getFirstname());
        //pollRepository.addUser(user);
        userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/LoginValidate")
    public String validateLogin(@RequestBody LoginCredentials loginCredentials) {
        System.out.println(loginCredentials.getEmail());
        System.out.println(loginCredentials.getPassword());
    }
}
