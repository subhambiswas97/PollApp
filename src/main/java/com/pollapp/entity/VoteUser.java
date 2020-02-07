package com.pollapp.entity;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "vote_user")
//@IdClass(value = VoteUserId.class)
public class VoteUser {

    @Id
    @Column(name="vote_user_id")
    @SequenceGenerator(name="voteUserSequenceGenerator", sequenceName = "voteUserSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voteUserSequenceGenerator")
    private Long optionId;


    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;


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
