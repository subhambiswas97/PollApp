package com.example.PollApp.validators;

import com.example.PollApp.exceptions.BadRequestException;
import com.example.PollApp.models.LoginCredentials;
import com.example.PollApp.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {

    private String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";


    public void validateUser(User user) throws BadRequestException {
        if(user.getFirstname().length() <2 && user.getFirstname().length() >15)
            throw new BadRequestException("Invalid Firstname");
        if(user.getLastname().length() <2 && user.getLastname().length() >15)
            throw new BadRequestException("Invalid Lastname");


        if(!user.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(user.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");
    }

    public void validateLogin(LoginCredentials loginCredentials) throws BadRequestException  {
        //String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(!loginCredentials.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(loginCredentials.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");

    }


}
