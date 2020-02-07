package com.pollapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "voter_records")
public class VoterRecord {

    @Id
    @Column(name = "voter_record_id")
    @SequenceGenerator(name="voterRecordSequenceGenerator", sequenceName = "voterRecordSequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voterRecordSequenceGenerator")
    private Long voterRecordId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @Column(name = "user_token")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public VoterRecord() {
    }

    public Long getVoterRecordId() {
        return voterRecordId;
    }

    public void setVoterRecordId(Long voterRecordId) {
        this.voterRecordId = voterRecordId;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
