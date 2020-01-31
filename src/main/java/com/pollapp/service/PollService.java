package com.pollapp.service;

import com.pollapp.controller.UserController;
import com.pollapp.dto.request.OptionDTO;
import com.pollapp.dto.request.PollDetailDTO;
import com.pollapp.dto.request.QuestionDTO;
import com.pollapp.dto.request.SingleQuesPollDTO;
import com.pollapp.entity.*;
import com.pollapp.exception.BadRequestException;
import com.pollapp.repository.OptionRepository;
import com.pollapp.repository.PollRepository;
import com.pollapp.repository.QuestionRepository;
import com.pollapp.repository.UserRepository;
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
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private UserRepository userRepository;


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

        try {

            Optional<Option> optionalOption = optionRepository.findById(optionId);
            if (optionalOption.isEmpty())
                return false;
            Option option = optionalOption.get();
            option.setVotes(option.getVotes()+1);
            optionRepository.save(option);
            return true;
        } catch (Exception e) {
            log.info(e.toString());
            return false;
        }

    }

    public String createSinglePoll(User user, SingleQuesPollDTO singleQuesPollDTO) throws BadRequestException {

        PollValidator.singleQuesPollValidator(singleQuesPollDTO);

        Poll poll = new Poll();
        poll.setPrivate(singleQuesPollDTO.isPrivate());
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


    public Question getQuestion(Long questionId) throws BadRequestException {


        PollValidator.validateQuestionId(questionId);
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if(optionalQuestion.isEmpty())
            return null;

        Question question = optionalQuestion.get();
        return question;
    }

    public boolean updateValidatedVote(User user, String pollId, Long questionId, Long optionId) throws Exception,BadRequestException {

            Optional<Option> optionalOption = optionRepository.findById(optionId);
            if (optionalOption.isEmpty())
                return false;
            Option option = optionalOption.get();

            PollValidator.checkOptionQuestionPollSame(pollId,questionId,option);


            if(option.getVotedBy().contains(user))
                throw new BadRequestException("Cannot vote twice");

            Poll poll = option.getQuestion().getPoll();
            PrivateVote privateVote = new PrivateVote();
            privateVote.setUserId(user.getId());
            privateVote.setPoll(poll);
            poll.addPrivateVotes(privateVote);

            option.setVotes(option.getVotes()+1);
            option.addVotedBy(user);
            user.addVotedOptions(option);
            optionRepository.save(option);
            return true;

    }

    public boolean hasPublicTokenVoted(Poll poll, String userToken) {

        List<PublicToken> votedTokens = poll.getPublicTokens();
        PublicToken publicToken;
        Iterator it = votedTokens.iterator();
        while(it.hasNext()) {
            publicToken = (PublicToken) it.next();
            if(publicToken.getToken().equals(userToken))
                return true;
        }
        return false;
    }

    public boolean hasPrivateVoted(Poll poll, Long userId) {

        List<PrivateVote> privateVotes = poll.getPrivateVotes();
        PrivateVote privateVote;
        Iterator it = privateVotes.iterator();
        while(it.hasNext()) {
            privateVote = (PrivateVote) it.next();
            if(privateVote.getUserId()==userId)
                return true;
        }
        return false;
    }

    public boolean updatePublicValidatedVote(String userToken, Long questionId, Long optionId) {


        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if (optionalOption.isEmpty())
            return false;
        Option option = optionalOption.get();

        Poll poll = option.getQuestion().getPoll();
        PublicToken publicToken = new PublicToken();
        publicToken.setPoll(poll);
        publicToken.setToken(userToken);
        poll.addPublicTokens(publicToken);
        option.setVotes(option.getVotes()+1);
        optionRepository.save(option);
        return true;
    }

    public void setEmbeddedPoll(User user, Poll poll) throws Exception {

        user.setEmbeddedPoll(poll);
        userRepository.save(user);
    }
}
