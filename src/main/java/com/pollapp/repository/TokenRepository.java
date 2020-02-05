package com.pollapp.repository;

import com.pollapp.entity.Token;
import com.pollapp.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token,Long> {

    @Modifying
    @Query("delete from Token t where t.token=:token")
    public void deleteByToken(@Param("token") String token);

    public Token findByToken(String token);
    public Optional<Token> findByUserId(Long userId);
}
