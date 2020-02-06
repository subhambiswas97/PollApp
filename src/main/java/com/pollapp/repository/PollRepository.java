package com.pollapp.repository;

import com.pollapp.entity.Poll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends CrudRepository<Poll,String> {

    public List<Poll> findAllByUserId(Long userId);


}
