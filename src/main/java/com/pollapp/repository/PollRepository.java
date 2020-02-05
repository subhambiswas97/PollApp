package com.pollapp.repository;

import com.pollapp.entity.Poll;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PollRepository extends CrudRepository<Poll,String> {

    public List<Poll> findAllByUserId(Long userId);
}
