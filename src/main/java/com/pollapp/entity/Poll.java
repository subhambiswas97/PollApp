package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @Column(name = "poll_id")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String pollId;

    @OneToMany(mappedBy = "poll",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("poll")
    private List<Question> questions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @JsonIgnoreProperties("polls")
    private User user;

    public Poll() {
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
}
