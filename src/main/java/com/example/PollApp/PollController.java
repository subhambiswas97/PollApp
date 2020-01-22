package com.example.PollApp;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(method = RequestMethod.GET, value = "/Users")
    public List<User> getUsers() {
        log.info("Get hitted");
        //return pollRepository.getUsers();
        return userService.getUsers();
    }

    //@CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/Users")
    //@CrossOrigin(origins = "http://localhost:3000")
    public void addUser(@RequestBody User user) {
        log.info("Post hitted");
        System.out.println(user.getEmail());
        System.out.println(user.getFirstname());
        //pollRepository.addUser(user);
        userService.addUser(user);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/LoginValidate")
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

    @RequestMapping(method = RequestMethod.POST, value = "/GetToken")
    public Response getToken(@RequestBody LoginCredentials loginCredentials) {
        System.out.println(loginCredentials.getEmail());
        System.out.println(loginCredentials.getPassword());

        Response response = new Response();
        String token = userService.getToken(loginCredentials);
        if(token == null)
            response.setStatus(204);
        else {
            response.setStatus(200);
            response.setToken(token);
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/GetUser")
    public User getUser(@RequestBody TokenWrapper tokenWrapper) {
        System.out.println(tokenWrapper.getToken());
        return userService.getUserByToken(tokenWrapper.getToken());
    }


}
