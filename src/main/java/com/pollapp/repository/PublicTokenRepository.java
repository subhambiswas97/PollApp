package com.pollapp.repository;

import com.pollapp.entity.PublicToken;
import com.pollapp.entity.PublicTokenId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicTokenRepository extends CrudRepository<PublicToken, PublicTokenId> {

    public Optional<PublicToken> findByTokenAndPollPollId(String token, String pollId);

    @Modifying
    @Query(value = "INSERT INTO public_tokens(poll_id, token) VALUES( ?1 , ?2 );", nativeQuery = true)
    public void recordPublicToken(String pollId, String userToken);


}
