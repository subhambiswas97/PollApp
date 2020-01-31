package com.pollapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SingleQuesPollDTO {

    private String token;

    @JsonProperty
    private boolean isPrivate;

    private String question;

    private List<String> options;

    public SingleQuesPollDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
