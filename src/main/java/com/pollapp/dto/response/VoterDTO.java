package com.pollapp.dto.response;

import com.pollapp.entity.User;

public class VoterDTO {

    private String firstName;

    private String lastName;

    private String email ;

    public VoterDTO() {
    }

    public VoterDTO(final User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
