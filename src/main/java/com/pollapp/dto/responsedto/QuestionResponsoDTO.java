package com.pollapp.dto.responsedto;

import com.pollapp.entity.Option;
import com.pollapp.entity.Question;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionResponsoDTO {

    private Long questionId;
    private String question;
    private List<OptionResponseDTO> options;

    public QuestionResponsoDTO() {
    }

    public QuestionResponsoDTO(final Question question) {
        this.questionId = question.getQuestionId();
        this.question = question.getQuestion();
        this.options = new ArrayList<>();
        Iterator it = question.getOptions().iterator();
        while (it.hasNext()) {
            this.options.add(new OptionResponseDTO((Option) it.next()));
        }
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<OptionResponseDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponseDTO> options) {
        this.options = options;
    }
}
