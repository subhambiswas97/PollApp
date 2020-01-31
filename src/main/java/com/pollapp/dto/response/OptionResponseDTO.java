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
    private List<VoterDTO> voterDTOS;

    public OptionResponseDTO() {
    }

    public OptionResponseDTO(final Option option, boolean isPrivate) {
        this.optionId = option.getOptionId();
        this.option = option.getOption();
        this.votes = option.getVotes();
        this.voterDTOS = new ArrayList<>();
        if(isPrivate) {
            //System.out.println("It is Private");
            Iterator it = option.getVotedBy().iterator();
            while(it.hasNext()) {
                this.voterDTOS.add(new VoterDTO((User) it.next()));
            }
        }

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

    public List<VoterDTO> getVoterDTOS() {
        return voterDTOS;
    }

    public void setVoterDTOS(List<VoterDTO> voterDTOS) {
        this.voterDTOS = voterDTOS;
    }
}
