package com.pollapp.repository;

import com.pollapp.entity.PublicToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicTokenRepository extends CrudRepository<PublicToken,Long> {

    public Optional<PublicToken> findByTokenAndPollPollId(String token, String pollId);
}
