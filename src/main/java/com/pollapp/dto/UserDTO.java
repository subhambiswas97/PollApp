package com.pollapp.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class UserDTO {


    private String email;


    private String firstName;


    private String lastName;


    private String password;
}
