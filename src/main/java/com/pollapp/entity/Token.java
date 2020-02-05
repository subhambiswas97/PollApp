package com.pollapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name="tokenSequenceGenerator", sequenceName = "tokenSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokenSequenceGenerator")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String token;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    //@MapsId
    //@JsonIgnoreProperties("token")
    private User user;

    public Token() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
