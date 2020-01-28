package com.pollapp.service;

import com.pollapp.controller.UserController;
import com.pollapp.dto.OptionDTO;
import com.pollapp.dto.PollDetailDTO;
import com.pollapp.dto.QuestionDTO;
import com.pollapp.dto.SingleQuesPollDTO;
import com.pollapp.entity.Option;
import com.pollapp.entity.Poll;
import com.pollapp.entity.Question;
import com.pollapp.entity.User;
import com.pollapp.exception.BadRequestException;
import com.pollapp.repository.OptionRepository;
import com.pollapp.repository.PollRepository;
import com.pollapp.validator.PollValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;

@Service
@Transactional
public class PollService {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private OptionRepository optionRepository;

    public String createPoll(User user, PollDetailDTO pollDetailDTO) throws BadRequestException {
        Poll poll = new Poll();

        PollValidator.validatePoll(pollDetailDTO);

        List<QuestionDTO> questionDTOS = pollDetailDTO.getQuestions();
        Iterator questionDTOIterator = questionDTOS.iterator();

        List<Question> questions = new ArrayList<>();

        while (questionDTOIterator.hasNext()) {
            QuestionDTO questionDTO = (QuestionDTO) questionDTOIterator.next();
            PollValidator.validateQuestion(questionDTO);
            Question question = new Question();
            question.setQuestion(questionDTO.getQuestion());

            List<Option> options = new ArrayList<>();
            List<OptionDTO> optionDTOS = questionDTO.getOptions();
            Iterator optionDTOIterator = optionDTOS.iterator();

            while (optionDTOIterator.hasNext()) {
                OptionDTO optionDTO = (OptionDTO) optionDTOIterator.next();
                PollValidator.validateOption(optionDTO);
                Option option = new Option();
                option.setOption(optionDTO.getOption());
                option.setQuestion(question);
                options.add(option);
            }
            question.setOptions(options);
            question.setPoll(poll);
            questions.add(question);
        }

        poll.setQuestions(questions);
        poll.setUser(user);
        user.addPolls(poll);

        String pollID = UUID.randomUUID().toString();
        poll.setPollId(pollID);
        pollRepository.save(poll);
        return pollID;
    }

    public Poll getPoll(String pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if(optionalPoll.isEmpty())
            return null;
        return optionalPoll.get();

    }

    public boolean updateVote(Long questionId, Long optionId) {
        boolean result = false;
        try {
            //optionRepository.updateVoteByQuestionIdAndOptionId(questionId,optionId);
            Optional<Option> optionalOption = optionRepository.findById(optionId);
            if (optionalOption.isEmpty())
                return false;
            Option option = optionalOption.get();
            option.setVotes(option.getVotes()+1);
            optionRepository.save(option);
            return true;
        } catch (Exception e) {
            log.info(e.toString());
            result = false;
        }
        return result;
    }

    public String createSinglePoll(User user, SingleQuesPollDTO singleQuesPollDTO) throws BadRequestException {

        PollValidator.singleQuesPollValidator(singleQuesPollDTO);

        Poll poll = new Poll();
        List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setQuestion(singleQuesPollDTO.getQuestion());
        Iterator it = singleQuesPollDTO.getOptions().iterator();
        List<Option> options = new ArrayList<>();
        while (it.hasNext()) {

            Option option = new Option();
            option.setOption((String) it.next());
            System.out.println(option.getOption());
            option.setQuestion(question);

            options.add(option);
        }
        question.setOptions(options);
        question.setPoll(poll);
        questions.add(question);

        poll.setQuestions(questions);
        poll.setUser(user);
        user.addPolls(poll);

        String pollID = UUID.randomUUID().toString();
        poll.setPollId(pollID);
        pollRepository.save(poll);
        return pollID;
    }
}
