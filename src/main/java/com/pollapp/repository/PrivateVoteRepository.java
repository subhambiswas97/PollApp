package com.pollapp.repository;

import com.pollapp.entity.PrivateVote;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrivateVoteRepository extends CrudRepository<PrivateVote,Long> {

    public Optional<PrivateVote> findByUserIdAndPollPollId(Long userId, String pollId);
}
