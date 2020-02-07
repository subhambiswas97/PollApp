package com.pollapp.service;

import com.pollapp.controller.UserController;
import com.pollapp.dto.CountDTO;
import com.pollapp.dto.request.OptionDTO;
import com.pollapp.dto.request.PollDetailDTO;
import com.pollapp.dto.request.QuestionDTO;
import com.pollapp.dto.request.SingleQuesPollDTO;
import com.pollapp.dto.response.OptionResponseDTO;
import com.pollapp.dto.response.PollResponseDTO;
import com.pollapp.dto.response.QuestionResponsoDTO;
import com.pollapp.dto.response.VoterDTO;
import com.pollapp.entity.*;
import com.pollapp.exception.BadRequestException;
import com.pollapp.repository.*;
import com.pollapp.validator.PollValidator;
import org.apache.catalina.webresources.JarResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.math.BigInteger;
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

//    @Autowired
//    private PublicTokenRepository publicTokenRepository;
//
//    @Autowired
//    private PrivateVoteRepository privateVoteRepository;

    @Autowired
    private VoteUserRepository voteUserRepository;

    @Autowired
    private VoterRecordRepository voterRecordRepository;


    public String createPoll(User user, PollDetailDTO pollDetailDTO) throws BadRequestException {
        PollValidator.validatePoll(pollDetailDTO);

        Poll poll = new Poll();
        String pollID = UUID.randomUUID().toString();
        poll.setPollId(pollID);
        poll.setUser(user);

        List<QuestionDTO> questionDTOS = pollDetailDTO.getQuestions();
        Iterator questionDTOIterator = questionDTOS.iterator();

        //List<Question> questions = new ArrayList<>();

        while (questionDTOIterator.hasNext()) {
            QuestionDTO questionDTO = (QuestionDTO) questionDTOIterator.next();
            PollValidator.validateQuestion(questionDTO);
            Question question = new Question();
            question.setQuestion(questionDTO.getQuestion());
            question.setPoll(poll);

            //List<Option> options = new ArrayList<>();
            List<OptionDTO> optionDTOS = questionDTO.getOptions();
            Iterator optionDTOIterator = optionDTOS.iterator();

            while (optionDTOIterator.hasNext()) {
                OptionDTO optionDTO = (OptionDTO) optionDTOIterator.next();
                PollValidator.validateOption(optionDTO);
                Option option = new Option();
                option.setOption(optionDTO.getOption());
                option.setQuestion(question);
                option.setVotes(0);
                optionRepository.save(option);
                //options.add(option);
            }
            //question.setOptions(options);

            //questions.add(question);
        }

        //poll.setQuestions(questions);

        //user.addPolls(poll);


        //pollRepository.save(poll);
        return pollID;
    }

    public PollResponseDTO getPollResponseDTO(String pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isEmpty())
            return null;

        Poll poll = optionalPoll.get();
        PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
        QuestionResponsoDTO questionResponsoDTO;
        OptionResponseDTO optionResponseDTO;
        Question question;
        Option option;
        List<Question> questionList;
        List<Option> optionList;
        Iterator questionIterator;
        Iterator optionIterator;
        questionList = questionRepository.findAllByPollPollId(pollId);
        questionIterator = questionList.iterator();
        while (questionIterator.hasNext()) {
            question = (Question) questionIterator.next();
            questionResponsoDTO = new QuestionResponsoDTO(question);

            optionList = optionRepository.findAllByQuestionQuestionId(question.getQuestionId());
            optionIterator = optionList.iterator();
            while (optionIterator.hasNext()) {
                option = (Option) optionIterator.next();
                optionResponseDTO = new OptionResponseDTO(option);
                questionResponsoDTO.addOptions(optionResponseDTO);
            }
            pollResponseDTO.addQuestions(questionResponsoDTO);
        }

        return pollResponseDTO;
    }

    public PollResponseDTO getPollResponseDTO(Poll poll) {
        if (poll==null)
            return null;
        String pollId = poll.getPollId();
        PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
        QuestionResponsoDTO questionResponsoDTO;
        OptionResponseDTO optionResponseDTO;
        Question question;
        Option option;
        List<Question> questionList;
        List<Option> optionList;
        Iterator questionIterator;
        Iterator optionIterator;
        questionList = questionRepository.findAllByPollPollId(pollId);
        questionIterator = questionList.iterator();
        while (questionIterator.hasNext()) {
            question = (Question) questionIterator.next();
            questionResponsoDTO = new QuestionResponsoDTO(question);

            optionList = optionRepository.findAllByQuestionQuestionId(question.getQuestionId());
            optionIterator = optionList.iterator();
            while (optionIterator.hasNext()) {
                option = (Option) optionIterator.next();
                optionResponseDTO = new OptionResponseDTO(option);
                questionResponsoDTO.addOptions(optionResponseDTO);
            }
            pollResponseDTO.addQuestions(questionResponsoDTO);
        }

        return pollResponseDTO;
    }

    public Poll getPoll(String pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isEmpty())
            return null;
        return optionalPoll.get();
    }

    public Question getQuestion(Long questionId) throws BadRequestException {


        PollValidator.validateQuestionId(questionId);
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if (optionalQuestion.isEmpty())
            return null;

        Question question = optionalQuestion.get();
        return question;
    }

    public Option getOption(Long optionId) {
        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if (optionalOption.isEmpty())
            return null;
        return optionalOption.get();
    }

    public List<VoterDTO> getVoters(Long optionId) {

        List<VoteUser> voteUserList = voteUserRepository.findByOptionOptionId(optionId);

        List<VoterDTO> voterDTOList = new ArrayList<>();

        if(voteUserList.isEmpty())
            return voterDTOList;

        User user;

        Iterator it = voteUserList.iterator();
        while (it.hasNext()) {
            user = ((VoteUser) it.next()).getUser();
            voterDTOList.add(new VoterDTO(user));
        }

        return voterDTOList;
    }

    public boolean updateVote(Question question, Option option) {

        try {

//            Optional<Option> optionalOption = optionRepository.findById(optionId);
//            if (optionalOption.isEmpty())
//                return false;
//            Option option = optionalOption.get();
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
        String pollID = UUID.randomUUID().toString();
        poll.setPollId(pollID);
        poll.setUser(user);

        //List<Question> questions = new ArrayList<>();
        Question question = new Question();
        question.setQuestion(singleQuesPollDTO.getQuestion());
        question.setPoll(poll);

        Iterator it = singleQuesPollDTO.getOptions().iterator();
        //List<Option> options = new ArrayList<>();
        while (it.hasNext()) {

            Option option = new Option();
            option.setOption((String) it.next());
            log.info(option.getOption());
            option.setQuestion(question);
            option.setVotes(0);

            optionRepository.save(option);
            //options.add(option);
        }
        //question.setOptions(options);

        //questions.add(question);

        //poll.setQuestions(questions);

        //user.addPolls(poll);


        //pollRepository.save(poll);
        return pollID;
    }


    public boolean updateValidatedVote(User user,
                                       Poll poll,
                                       Question question,
                                       Option option) throws Exception, BadRequestException {

//        Optional<Object> optionalCountDTO = questionRepository.getOptionQuestionPollValidation(optionId,questionId,pollId);
//        if(optionalCountDTO.isEmpty())
//            throw new Exception("Query Fetch Error");
//        else if(((BigInteger)optionalCountDTO.get()).longValue()==0)
//            throw new Exception("Question or Option doesnot belong to the same poll");
//
//        Optional<Option> optionalOption = optionRepository.findById(optionId);
//        if (optionalOption.isEmpty())
//            return false;
//        Option option = optionalOption.get();



        //PollValidator.checkOptionQuestionPollSame(pollId, questionId, option);

//        if (option.getVotedBy().contains(user))
//            throw new BadRequestException("Cannot vote twice");
//        Poll poll = option.getQuestion().getPoll();
        //PrivateVote privateVote = new PrivateVote();
        //privateVote.setUserId(user.getId());
        //privateVote.setPoll(poll);
        //poll.addPrivateVotes(privateVote);

        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);

        log.info("Vote Checkpoint 1");

        VoteUser voteUser = new VoteUser();
        voteUser.setUser(user);
        voteUser.setOption(option);
        voteUser.setPoll(poll);

//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        date = cal.getTime();

        voteUser.setDate(new Date());
        voteUserRepository.save(voteUser);

        log.info("Vote Checkpoint 2");

        VoterRecord voterRecord = new VoterRecord();
        voterRecord.setPoll(poll);
        //voterRecord.setPrivate(poll.isPrivate());
        voterRecord.setUser(user);
        voterRecord.setToken(null);
        voterRecordRepository.save(voterRecord);

        log.info("Vote Checkpoint 3");

        //VoteUser voteUser = new VoteUser();
        //voteUser.setOption(option);
        //voteUser.setUser(user);

        //privateVoteRepository.save(privateVote);
        //optionRepository.save(option);
        //int result = optionRepository.updateVoteByQuestionIdAndOptionIdAndPollId(optionId,questionId,pollId);
        //log.info("Private  vote update : "+ result);
        //if(result==0)
        //    throw new Exception("Option/Question/Poll Id not same ");
        //optionRepository.updateUserVotes(optionId,user.getId());
        //privateVoteRepository.recordPrivateVote(pollId,user.getId());
        //log.info("optionId : " + optionId + " userId" + user.getId());
        //result = voteUserRepository.addVoteUser(optionId,user.getId());
        //voteUserRepository.save(voteUser);
        //log.info("Vote Recorded : " + result);
        return true;

    }

    public boolean hasPublicTokenVoted(String pollId, String userToken) {

//        Optional<PublicToken> optionalPublicToken = publicTokenRepository.findByTokenAndPollPollId(userToken,pollId);
//        if(optionalPublicToken.isEmpty())
//            return false;
//        return true;
        Optional<VoterRecord> optionalVoterRecord = voterRecordRepository.findByTokenAndPollPollId(userToken,pollId);
        if(optionalVoterRecord.isEmpty())
            return false;
        return true;
    }

    public boolean hasPrivateVoted(String pollId, User user) {

//        Optional<PrivateVote> optionalPrivateVote = privateVoteRepository.findByUserIdAndPollPollId(userId,pollId);
//        if(optionalPrivateVote.isEmpty())
//            return false;
//        return true;
        Optional<VoterRecord> optionalVoterRecord = voterRecordRepository.findByUserAndPollPollId(user,pollId);
        if(optionalVoterRecord.isEmpty())
            return false;
        return true;
    }

    public boolean updatePublicValidatedVote(String userToken, Poll poll, Question question, Option option) throws Exception {


//        Optional<Object> optionalCountDTO = questionRepository.getOptionQuestionPollValidation(optionId,questionId,pollId);
//        if(optionalCountDTO.isEmpty())
//            throw new Exception("Query Fetch Error");
//        else if(((BigInteger)optionalCountDTO.get()).longValue()==0)
//            throw new Exception("Question or Option doesnot belong to the same poll");
//
//        Optional<Option> optionalOption = optionRepository.findById(optionId);
//        if (optionalOption.isEmpty())
//            return false;
//        Option option = optionalOption.get();
//
//        Poll poll = option.getQuestion().getPoll();

        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);

        VoteUser voteUser = new VoteUser();
        voteUser.setUser(null);
        voteUser.setOption(option);
        voteUser.setPoll(poll);

//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        date = cal.getTime();

        voteUser.setDate(new Date());
        voteUserRepository.save(voteUser);

        VoterRecord voterRecord = new VoterRecord();
        voterRecord.setToken(userToken);
        voterRecord.setUser(null);
       // voterRecord.setPrivate(poll.isPrivate());
        voterRecord.setPoll(poll);
        voterRecordRepository.save(voterRecord);

        //PublicToken publicToken = new PublicToken();
        //publicToken.setPoll(poll);
        //publicToken.setToken(userToken);
        //poll.addPublicTokens(publicToken);
        //option.setVotes(option.getVotes() + 1);
        //publicTokenRepository.save(publicToken);
        //int result = optionRepository.updateVoteByQuestionIdAndOptionIdAndPollId(optionId,questionId,pollId);
        //log.info("Public Token update : "+ result);
        //if(result==0)
        //    throw new Exception("Option/Question/Poll Id not same ");
        //publicTokenRepository.recordPublicToken(pollId,userToken);
        //optionRepository.save(option);
        return true;
    }

    public void setEmbeddedPoll(Poll poll,User user) throws Exception {

        //user.setEmbeddedPoll(poll);
        //userRepository.save(user);
        //userRepository.setEmbeddedPoll(pollId, userId);
        user.setEmbeddedPoll(poll);
        userRepository.save(user);
    }

    public boolean updateMultiVote(boolean isPrivate,
                                         Object userTokenOrUser,
                                         Poll poll,
                                         List<Long> questionIds,
                                         List<Long> optionIds) throws Exception, BadRequestException {
        int arrSize = questionIds.size();

        Optional<Option> optionalOption;
        Option option;
        Question question;
        for(int i=0;i<arrSize;i++) {

            optionalOption = optionRepository.findById(optionIds.get(i));
            if(optionalOption.isEmpty())
                throw new BadRequestException("Invalid OptionId");

            option = optionalOption.get();
            question = option.getQuestion();
            if(question.getQuestionId()!=questionIds.get(i))
                throw new BadRequestException("Option/Question Id not matching");

            if(question.getPoll().getPollId()!=poll.getPollId())
                throw new BadRequestException("Option/Poll Id not matching");

            option.setVotes(option.getVotes() + 1 );
            optionRepository.save(option);

            VoteUser voteUser = new VoteUser();
            voteUser.setOption(option);
            voteUser.setPoll(poll);

//            Date date = new Date();
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            cal.set(Calendar.HOUR_OF_DAY, 0);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            date = cal.getTime();

            voteUser.setDate(new Date());

            if(isPrivate) {
                voteUser.setUser((User) userTokenOrUser);
            } else {
                voteUser.setUser(null);
            }

            voteUserRepository.save(voteUser);

        }


        //pollRepository.save(poll);

//        if(isPrivate) {
//            PrivateVote privateVote = new PrivateVote();
//            privateVote.setUserId(((User) userTokenOrUser).getId());
//            privateVote.setPoll(poll);
//            //poll.addPrivateVotes(privateVote);
//            privateVoteRepository.save(privateVote);
//        }
//        else {
//            PublicToken publicToken = new PublicToken();
//            publicToken.setPoll(poll);
//            publicToken.setToken((String) userTokenOrUser);
//            //poll.addPublicTokens(publicToken);
//            publicTokenRepository.save(publicToken);
//        }

        VoterRecord voterRecord = new VoterRecord();
        //voterRecord.setPrivate(poll.isPrivate());
        voterRecord.setPoll(poll);
        if(poll.isPrivate()) {
            voterRecord.setUser((User) userTokenOrUser);
            voterRecord.setToken(null);
        } else {
            voterRecord.setUser(null);
            voterRecord.setToken((String) userTokenOrUser);
        }
        voterRecordRepository.save(voterRecord);

        return true;
    }

    public List<Poll> getUserPolls(Long userId) {
        List<Poll> polls = pollRepository.findAllByUserId(userId);
        return polls;
    }
}
