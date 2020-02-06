package com.pollapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @Column(name = "question_id")
    @SequenceGenerator(name="questionSequenceGenerator", sequenceName = "questionSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionSequenceGenerator")
    private Long questionId;

    @NotNull
    @Column(name = "_question")
    private String question;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    @JsonIgnoreProperties("questions")
    private Poll poll;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("question")
    private List<Option> options;

    public Question() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
