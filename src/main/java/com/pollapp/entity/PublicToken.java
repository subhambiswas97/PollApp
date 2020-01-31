package com.pollapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "public_tokens")
public class PublicToken {

    @Id
    @Column(name = "pubic_token_id")
    @SequenceGenerator(name="publicTokenSequenceGenerator", sequenceName = "publicTokenSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publicTokenSequenceGenerator")
    private Long publicTokenId;

    @NotNull
    private String token;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
    @JsonIgnoreProperties("publicTokens")
    private Poll poll;

    public PublicToken() {
    }

    public Long getPublicTokenId() {
        return publicTokenId;
    }

    public void setPublicTokenId(Long publicTokenId) {
        this.publicTokenId = publicTokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}
