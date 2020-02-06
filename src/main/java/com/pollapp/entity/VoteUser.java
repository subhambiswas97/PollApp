package com.pollapp.entity;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "vote_user")
@IdClass(value = VoteUserId.class)
public class VoteUser {

    @Id
    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Id
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public VoteUser() {
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
