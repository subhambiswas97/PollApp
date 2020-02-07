package com.pollapp.repository;

import com.pollapp.entity.Option;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends CrudRepository<Option,Long> {

    @Modifying
    @Query(value = "UPDATE options o SET o.votes=o.votes+1 WHERE o.question_id=?1 AND o.option_id=?2", nativeQuery = true)
    public int updateVoteByQuestionIdAndOptionId(Long questionId, Long optionId);

//    @Modifying
//    @Query(value = "INSERT INTO vote_user(option_id, user_id) VALUES (?1 , ?2)", nativeQuery = true)
//    public int updateUserVotes(Long optionId, Long userId);

    @Modifying
    @Query(value = "UPDATE options o JOIN questions q ON o.question_id = q.question_id SET o.votes=o.votes+1 where o.option_id=?1 AND o.question_id=?2 AND q.poll_id=?3",nativeQuery = true)
    public int updateVoteByQuestionIdAndOptionIdAndPollId(Long optionId, Long questionId, String pollId);

    public List<Option> findAllByQuestionQuestionId(Long QuestionId);

    //@Query(value = "select * from option ")
    //public Option findByQuestionIdAndOptionId(@Param("question_id") Long questionId,@Param("option_id") Long optionId);
    //public Option findByQuestionIdAndOptionId( Long questionId, Long optionId);
}
