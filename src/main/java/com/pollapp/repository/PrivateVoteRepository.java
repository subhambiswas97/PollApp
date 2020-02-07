//package com.pollapp.repository;
//
//import com.pollapp.entity.PrivateVote;
//import com.pollapp.entity.PrivateVoteId;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface PrivateVoteRepository extends CrudRepository<PrivateVote, PrivateVoteId> {
//
//    public Optional<PrivateVote> findByUserIdAndPollPollId(Long userId, String pollId);
//
//    @Modifying
//    @Query(value = "INSERT INTO private_votes(poll_id, user_id) VALUES( ?1 , ?2 );", nativeQuery = true)
//    public void recordPrivateVote(String pollId, Long userId);
//}
