package com.pollapp.repository;

import com.pollapp.entity.VoteUser;
import com.pollapp.entity.VoteUserId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteUserRepository extends CrudRepository<VoteUser,Long> {

    //@Modifying
    //@Query(value = "INSERT INTO vote_user(option_id, user_id) VALUES( ?1 , ?2 )", nativeQuery = true)
    //public int addVoteUser(Long optionId, Long userId);


    public List<VoteUser> findByOptionOptionId(Long optionId);

}
