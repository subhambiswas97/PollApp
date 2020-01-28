package com.pollapp.repository;

import com.pollapp.entity.Poll;
import org.springframework.data.repository.CrudRepository;

public interface PollRepository extends CrudRepository<Poll,String> {
}
