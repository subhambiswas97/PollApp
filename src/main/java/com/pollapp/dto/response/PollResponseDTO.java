package com.pollapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pollapp.entity.Poll;
import com.pollapp.entity.Question;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PollResponseDTO {

    private String pollId;
    @JsonProperty
    private boolean isPrivate;
    private List<QuestionResponsoDTO> questions;

    public PollResponseDTO() {
    }

    public PollResponseDTO(final Poll poll,boolean isPrivate) {
        this.pollId = poll.getPollId();
        this.isPrivate = poll.isPrivate();
        this.questions = new ArrayList<>();
        Iterator it = poll.getQuestions().iterator();
        while (it.hasNext()) {
            questions.add(new QuestionResponsoDTO((Question) it.next() , isPrivate));
        }

    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<QuestionResponsoDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponsoDTO> questions) {
        this.questions = questions;
    }
}
