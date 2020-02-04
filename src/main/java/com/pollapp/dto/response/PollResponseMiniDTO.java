package com.pollapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pollapp.entity.Poll;
import com.pollapp.entity.Question;

import java.util.ArrayList;
import java.util.Iterator;

public class PollResponseMiniDTO {

    private String pollId;

    @JsonProperty
    private boolean isPrivate;

    public PollResponseMiniDTO() {
    }

    public PollResponseMiniDTO(final Poll poll) {
        this.pollId = poll.getPollId();
        this.isPrivate = poll.isPrivate();
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
}
