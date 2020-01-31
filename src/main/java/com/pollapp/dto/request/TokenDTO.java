package com.pollapp.dto.request;

public class TokenDTO {

    private  String userToken;

    public TokenDTO() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
