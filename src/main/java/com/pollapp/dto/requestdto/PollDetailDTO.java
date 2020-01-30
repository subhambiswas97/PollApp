package com.pollapp.dto.requestdto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PollDetailDTO {

    private String token;

    private boolean isPrivate;

    private List<QuestionDTO> questions;

    public PollDetailDTO() {
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

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
