package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @Column(name="option_id")
    @SequenceGenerator(name="optionSequenceGenerator", sequenceName = "optionSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "optionSequenceGenerator")
    private Long optionId;

    @NotNull
    @Column(name = "_option")
    private String option;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @JsonIgnoreProperties("options")
    private Question question;

    @Column(name = "votes")
    private int votes = 0;

    @ManyToMany
    @JoinTable(name = "vote_user",
    joinColumns = @JoinColumn(name = "option_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties("votedOptions")
    private List<User> votedBy;

    public Option() {
        this.votedBy = new ArrayList<>();
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public List<User> getVotedBy() {
        return votedBy;
    }

    public void setVotedBy(List<User> votedBy) {
        this.votedBy = votedBy;
    }

    public void addVotedBy(User user) {
        this.votedBy.add(user);
    }
}
