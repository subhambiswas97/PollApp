package com.pollapp.dto.requestdto;

import java.util.List;

public class MultiVoteDetailDTO {

    private List<Long> questionIds;

    private List<Long> optionIds;

    public MultiVoteDetailDTO() {
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Long> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Long> optionIds) {
        this.optionIds = optionIds;
    }
}
