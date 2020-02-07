package com.pollapp.dto.response;

import com.pollapp.entity.Option;
import com.pollapp.entity.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OptionResponseDTO {

    private Long optionId;
    private String option;
    private int votes;

    public OptionResponseDTO() {
    }

    public OptionResponseDTO(final Option option) {
        this.optionId = option.getOptionId();
        this.option = option.getOption();
        this.votes = option.getVotes();

    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

}
