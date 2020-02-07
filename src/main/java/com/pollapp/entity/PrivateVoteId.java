//package com.pollapp.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class PrivateVoteId implements Serializable {
//
//    private Long userId;
//
//    private Poll poll;
//
//    public PrivateVoteId() {
//    }
//
//    public PrivateVoteId(Long userId, Poll poll) {
//        this.userId = userId;
//        this.poll = poll;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o )
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        PrivateVoteId privateVoteId = (PrivateVoteId) o;
//        return userId.equals(privateVoteId.userId) && poll.equals(privateVoteId.poll);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userId,poll);
//    }
//
//}
