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
import org.apache.catalina.webresources.JarResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;

@Service
@Transactional
public class PollService {

    private static final Logger log = Logger.getLogger(PollService.class.getName());

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private UserRepository userRepository;


    public String createPoll(User user, PollDetailDTO pollDetailDTO) throws BadRequestException {
        PollValidator.validatePoll(pollDetailDTO);

        Poll poll = new Poll();

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
        if (optionalPoll.isEmpty())
            return null;
        return optionalPoll.get();

    }

    public boolean updateVote(Long questionId, Long optionId) {

        try {

            Optional<Option> optionalOption = optionRepository.findById(optionId);
            if (optionalOption.isEmpty())
                return false;
            Option option = optionalOption.get();
            option.setVotes(option.getVotes() + 1);
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

        if (optionalQuestion.isEmpty())
            return null;

        Question question = optionalQuestion.get();
        return question;
    }

    public boolean updateValidatedVote(User user,
                                       String pollId,
                                       Long questionId,
                                       Long optionId) throws Exception, BadRequestException {

        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if (optionalOption.isEmpty())
            return false;
        Option option = optionalOption.get();

        PollValidator.checkOptionQuestionPollSame(pollId, questionId, option);


        if (option.getVotedBy().contains(user))
            throw new BadRequestException("Cannot vote twice");

        Poll poll = option.getQuestion().getPoll();
        PrivateVote privateVote = new PrivateVote();
        privateVote.setUserId(user.getId());
        privateVote.setPoll(poll);
        poll.addPrivateVotes(privateVote);

        option.setVotes(option.getVotes() + 1);
        option.addVotedBy(user);
        user.addVotedOptions(option);
        optionRepository.save(option);
        return true;

    }

    public boolean hasPublicTokenVoted(Poll poll, String userToken) {

        List<PublicToken> votedTokens = poll.getPublicTokens();
        PublicToken publicToken;
        Iterator it = votedTokens.iterator();
        while (it.hasNext()) {
            publicToken = (PublicToken) it.next();
            if (publicToken.getToken().equals(userToken))
                return true;
        }
        return false;
    }

    public boolean hasPrivateVoted(Poll poll, Long userId) {

        List<PrivateVote> privateVotes = poll.getPrivateVotes();
        PrivateVote privateVote;
        Iterator it = privateVotes.iterator();
        while (it.hasNext()) {
            privateVote = (PrivateVote) it.next();
            if (privateVote.getUserId() == userId)
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
        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);
        return true;
    }

    public void setEmbeddedPoll(User user, Poll poll) throws Exception {

        user.setEmbeddedPoll(poll);
        userRepository.save(user);
    }

    public boolean updateMultiVote(boolean isPrivate,
                                         Object userTokenOrUser,
                                         Poll poll,
                                         List<Long> questionIds,
                                         List<Long> optionIds) throws Exception, BadRequestException {
        int arrSize = questionIds.size();
        /*for(int i=0;i<arrSize;i++) {
            Optional<Option> optionalOption = optionRepository.findById(optionIds.get(i));
            if(optionalOption.isEmpty())
                throw new BadRequestException("Option ID doesnot Exist");
            Option option = optionalOption.get();
            PollValidator.checkOptionQuestionPollSame(poll.getPollId(),questionIds.get(i),option);
        }
        */

        HashMap<Long, Long> questionOptionMap = new HashMap<>();
        HashMap<Long, Boolean> visitedQuestion = new HashMap<>();
        HashMap<Long, Boolean> visitedOptions = new HashMap<>();
        for (int i = 0; i < arrSize; i++) {
            questionOptionMap.put(questionIds.get(i), optionIds.get(i));
            visitedQuestion.put(questionIds.get(i), false);
            visitedOptions.put(optionIds.get(i), false);
        }

        List<Question> pollQuestions = poll.getQuestions();
        int questionListSize = pollQuestions.size();
        for (int i = 0; i < questionListSize; i++) {
            Question question = pollQuestions.get(i);
            Long questionId = question.getQuestionId();
            visitedQuestion.put(questionId, true);

            List<Option> optionList = question.getOptions();
            int optionListSize = optionList.size();
            for (int j = 0; j < optionListSize; j++) {
                Option option = optionList.get(j);
                Long optionId = option.getOptionId();

                if (questionOptionMap.get(questionId) == optionId) {
                    visitedOptions.put(optionId, true);
                    option.setVotes(option.getVotes() + 1);

                    if(isPrivate) {
                        option.addVotedBy((User) userTokenOrUser);
                        ((User) userTokenOrUser).addVotedOptions(option);
                    }

                    //optionList.set(j,option);
                    break;
                }
            }
            //question.setOptions(optionList);

            //pollQuestions.set(i,question);
        }
        //poll.setQuestions(pollQuestions);

        for (int i = 0; i < arrSize; i++) {
            if (!(visitedQuestion.get(questionIds.get(i)))) {
                throw new BadRequestException("Question Id : " + questionIds.get(i) + " Not Visited ");
            }
        }

        for (int i = 0; i < arrSize; i++) {
            if (!(visitedOptions.get(optionIds.get(i)))) {
                throw new BadRequestException("Option Id : " + optionIds.get(i) + " Not Visited ");
            }
        }
        if(isPrivate) {
            PrivateVote privateVote = new PrivateVote();
            privateVote.setUserId(((User) userTokenOrUser).getId());
            privateVote.setPoll(poll);
            poll.addPrivateVotes(privateVote);
        }
        else {
            PublicToken publicToken = new PublicToken();
            publicToken.setPoll(poll);
            publicToken.setToken((String) userTokenOrUser);
            poll.addPublicTokens(publicToken);
        }

        pollRepository.save(poll);

        return true;
    }
}
