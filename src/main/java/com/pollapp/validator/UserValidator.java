package com.pollapp.validator;

import com.pollapp.dto.request.RegisterDTO;
import com.pollapp.exception.BadRequestException;
import com.pollapp.dto.request.LoginDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserValidator {

    private static final String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public static void validateUserToken(String token) throws BadRequestException {
        try {
            UUID uuid = UUID.fromString(token);
            if(!(uuid.toString().equals(token)))
                throw new BadRequestException("Invalid Token");
        } catch (Exception e) {
            throw new BadRequestException("Invalid Token");
        }
    }

    public static void validateUser(RegisterDTO registerDTO) throws BadRequestException {
        if(registerDTO.getFirstName().length() <2 && registerDTO.getFirstName().length() >15)
            throw new BadRequestException("Invalid Firstname");
        if(registerDTO.getLastName().length() <2 && registerDTO.getLastName().length() >15)
            throw new BadRequestException("Invalid Lastname");


        if(!registerDTO.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(registerDTO.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");
    }

    public static void validateLogin(LoginDTO loginDTO) throws BadRequestException  {

        if(!loginDTO.getEmail().matches(regex))
            throw new BadRequestException("Invalid Email");

        if(loginDTO.getPassword().length() <8 )
            throw new BadRequestException("Invalid Password");
    }
}
