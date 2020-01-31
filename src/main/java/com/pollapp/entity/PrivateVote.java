package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "private_votes")
public class PrivateVote {

    @Id
    @Column(name = "private_vote_id")
    @SequenceGenerator(name="privateVoteSequenceGenerator", sequenceName = "privateVoteSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privateVoteSequenceGenerator")
    private Long privateVoteId;

    @NotNull
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
    @JsonIgnoreProperties("privateVotes")
    private Poll poll;

    public PrivateVote() {
    }

    public Long getPrivateVoteId() {
        return privateVoteId;
    }

    public void setPrivateVoteId(Long privateVoteId) {
        this.privateVoteId = privateVoteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
