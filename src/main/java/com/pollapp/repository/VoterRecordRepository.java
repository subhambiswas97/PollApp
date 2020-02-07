package com.pollapp.repository;

import com.pollapp.entity.Option;
import com.pollapp.entity.Poll;
import com.pollapp.entity.User;
import com.pollapp.entity.VoterRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRecordRepository extends CrudRepository<VoterRecord,Long> {

    public Optional<VoterRecord> findByUserAndPollPollId(User user, String pollId);

    public Optional<VoterRecord> findByTokenAndPollPollId(String token, String pollId);



}
