package com.pollapp.dto;

import com.pollapp.entity.Question;

import java.util.List;

public class PollDetailDTO {

    private String token;

    private List<QuestionDTO> questions;

    public PollDetailDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
