//package com.pollapp.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class VoteUserId implements Serializable {
//
//    private Option option;
//
//    private User user;
//
//    public VoteUserId() {
//    }
//
//    public VoteUserId(Option option, User user) {
//        this.option = option;
//        this.user = user;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o )
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        VoteUserId voteUserId = (VoteUserId) o;
//        return option.equals(voteUserId.option) && user.equals(voteUserId.user);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(option,user);
//    }
//}
