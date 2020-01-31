package com.pollapp.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String token;

    @OneToOne
    @MapsId
    @JsonIgnoreProperties("token")
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
