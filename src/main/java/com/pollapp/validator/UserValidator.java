package com.pollapp.validator;

import com.pollapp.exception.BadRequestException;
import com.pollapp.dto.LoginDTO;
import com.pollapp.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {

    private static final String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";


    public static void validateUser(User user) throws BadRequestException {
        if(user.getFirstName().length() <2 && user.getFirstName().length() >15)
            throw new BadRequestException("Invalid Firstname");
        if(user.getLastName().length() <2 && user.getLastName().length() >15)
            throw new BadRequestException("Invalid Lastname");


        if(!user.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(user.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");
    }

    public static void validateLogin(LoginDTO loginDTO) throws BadRequestException  {
        //String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(!loginDTO.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(loginDTO.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");
    }
}
