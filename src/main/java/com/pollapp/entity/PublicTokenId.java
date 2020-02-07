//package com.pollapp.entity;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//public class PublicTokenId implements Serializable {
//
//    private String token;
//
//    private Poll poll;
//
//    public PublicTokenId() {
//    }
//
//    public PublicTokenId(String token, Poll poll) {
//        this.token = token;
//        this.poll = poll;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o )
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        PublicTokenId publicTokenId = (PublicTokenId) o;
//        return token.equals(publicTokenId.token) && poll.equals(publicTokenId.poll);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(token,poll);
//    }
//}
