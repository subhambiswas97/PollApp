package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "private_votes")
@IdClass(value = PrivateVoteId.class)
public class PrivateVote implements Serializable {

//    @Id
//    @Column(name = "private_vote_id")
//    @SequenceGenerator(name="privateVoteSequenceGenerator", sequenceName = "privateVoteSequence", initialValue = 1,allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privateVoteSequenceGenerator")
//    private Long privateVoteId;

    @Id
    @NotNull
    private Long userId;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    @JsonIgnoreProperties("privateVotes")
    private Poll poll;

    public PrivateVote() {
    }

//    public Long getPrivateVoteId() {
//        return privateVoteId;
//    }
//
//    public void setPrivateVoteId(Long privateVoteId) {
//        this.privateVoteId = privateVoteId;
//    }

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
