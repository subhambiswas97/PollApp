package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "email",unique = true)
    private String email;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private Token token;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<Poll> polls;

    @ManyToMany(mappedBy = "votedBy")
    @JsonIgnoreProperties("votedBy")
    private List<Option> votedOptions;

    @OneToOne
    @JoinColumn(name = "embedded_poll")
    @JsonIgnoreProperties("user")
    private Poll embeddedPoll;

    public User() {

        polls = new ArrayList<>();
        this.votedOptions = new ArrayList<>();
    }

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public void addPolls(Poll poll) { this.polls.add(poll); }

    public List<Option> getVotedOptions() {
        return votedOptions;
    }

    public void setVotedOptions(List<Option> votedOptions) {
        this.votedOptions = votedOptions;
    }

    public void addVotedOptions(Option option) {
        this.votedOptions.add(option);
    }

    public Poll getEmbeddedPoll() {
        return embeddedPoll;
    }

    public void setEmbeddedPoll(Poll embeddedPoll) {
        this.embeddedPoll = embeddedPoll;
    }
}
