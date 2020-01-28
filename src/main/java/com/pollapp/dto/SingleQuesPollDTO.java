package com.pollapp.dto;

import com.pollapp.entity.Question;

import java.util.List;

public class SingleQuesPollDTO {

    private String token;

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
