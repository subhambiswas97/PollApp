package com.pollapp.repository;

import com.pollapp.dto.CountDTO;
import com.pollapp.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question,Long> {

    @Query(value = "SELECT count(*)  FROM questions q INNER JOIN options o ON o.question_id = q.question_id where o.option_id=?1 and o.question_id=?2 and q.poll_id=?3", nativeQuery = true)
    public Optional<Object> getOptionQuestionPollValidation(Long optionId, Long questionId, String pollId);

    @Query(value = "SELECT count(*)  FROM questions q INNER JOIN options o ON o.question_id = q.question_id where o.option_id=?1 and o.question_id=?2", nativeQuery = true)
    public Optional<Object> getOptionQuestionValidation(Long optionId, Long questionId);
}
