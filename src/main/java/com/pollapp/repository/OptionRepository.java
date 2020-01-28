package com.pollapp.repository;

import com.pollapp.entity.Option;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OptionRepository extends CrudRepository<Option,Long> {

    //@Modifying
    //@Query(value = "UPDATE Option o SET o.votes=o.votes+1 WHERE o.question_id=:question_id AND o.option_id=:option_id")
    //public void updateVoteByQuestionIdAndOptionId(@Param("question_id") Long questionId,@Param("option_id") Long optionId);

    //@Query(value = "select * from option ")
    //public Option findByQuestionIdAndOptionId(@Param("question_id") Long questionId,@Param("option_id") Long optionId);
    //public Option findByQuestionIdAndOptionId( Long questionId, Long optionId);
}
