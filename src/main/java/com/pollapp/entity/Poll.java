package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.jdi.PrimitiveValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @Column(name = "poll_id")
    private String pollId;

    @Column(name = "is_private")
    private boolean isPrivate = false;

    @OneToMany(mappedBy = "poll",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("poll")
    private List<Question> questions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"polls","embedded_poll"})
    private User user;

    //@OneToMany(mappedBy = "poll",cascade = CascadeType.ALL)
    //@JsonIgnoreProperties("poll")
    //private List<PublicToken> publicTokens;

//    @OneToMany(mappedBy = "poll",cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("poll")
//    private List<PrivateVote> privateVotes;

    public Poll() {
        //this.publicTokens = new ArrayList<>();
        //this.privateVotes = new ArrayList<>();
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

//    public List<PublicToken> getPublicTokens() {
//        return publicTokens;
//    }
//
//    public void setPublicTokens(List<PublicToken> publicTokens) {
//        this.publicTokens = publicTokens;
//    }
//
//    public void addPublicTokens(PublicToken publicToken) {
//        this.publicTokens.add(publicToken);
//    }
//
//    public List<PrivateVote> getPrivateVotes() {
//        return privateVotes;
//    }
//
//    public void setPrivateVotes(List<PrivateVote> privateVotes) {
//        this.privateVotes = privateVotes;
//    }
//
//    public void addPrivateVotes(PrivateVote privateVote) {
//        this.privateVotes.add(privateVote);
//    }
}
