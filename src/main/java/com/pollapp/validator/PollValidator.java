package com.pollapp.validator;

import com.pollapp.dto.*;
import com.pollapp.dto.request.OptionDTO;
import com.pollapp.dto.request.PollDetailDTO;
import com.pollapp.dto.request.QuestionDTO;
import com.pollapp.dto.request.SingleQuesPollDTO;
import com.pollapp.entity.Option;
import com.pollapp.entity.Poll;
import com.pollapp.exception.BadRequestException;

import java.util.Iterator;
import java.util.UUID;

public class PollValidator {

    public static void validatePollToken(String token) throws BadRequestException {
        try {
            UUID uuid = UUID.fromString(token);
            if (!(uuid.toString().equals(token)))
                throw new BadRequestException("Invalid Token");
        } catch (Exception e) {
            throw new BadRequestException("Invalid Token");
        }

    }

    public static void validatePoll(PollDetailDTO pollDetailDTO) throws BadRequestException {
        PollValidator.validatePollToken(pollDetailDTO.getToken());

        if (pollDetailDTO.getQuestions().isEmpty())
            throw new BadRequestException("No Questions in Poll");
    }

    public static void validateQuestion(QuestionDTO questionDTO) throws BadRequestException {
        if (questionDTO.getQuestion().length() == 0)
            throw new BadRequestException("Question is Blank");
        if (questionDTO.getOptions().size() < 2)
            throw new BadRequestException("Minimum 2 Options needed");

    }

    public static void validateOption(OptionDTO optionDTO) throws BadRequestException {
        if (optionDTO.getOption().length() == 0)
            throw new BadRequestException("Option is blank");
    }

    public static void validateVote(VoteDetailDTO voteDetailDTO) throws BadRequestException {
        PollValidator.validatePollToken(voteDetailDTO.getPollId());

        if (voteDetailDTO.getQuestionId() <= 0)
            throw new BadRequestException("Invalid QuestionId");

        if (voteDetailDTO.getOptionId() <= 0)
            throw new BadRequestException("Invalid OptionId");

    }

    public static void validateQuestionId(Long questionId) throws BadRequestException {
        if (questionId <= 0)
            throw new BadRequestException("Invalid QuestionId");

    }

    public static void validateOptionId(Long optionId) throws BadRequestException {
        if (optionId <= 0)
            throw new BadRequestException("Invalid OptionId");
    }

    public static void singleQuesPollValidator(SingleQuesPollDTO singleQuesPollDTO) throws BadRequestException {

        //UserValidator.validateUserToken(singleQuesPollDTO.getToken());
        if (singleQuesPollDTO.getQuestion().length() == 0)
            throw new BadRequestException("Question is Blank");
        if (singleQuesPollDTO.getOptions().size() < 2)
            throw new BadRequestException("Minimum 2 optins required");
        Iterator it = singleQuesPollDTO.getOptions().iterator();
        while (it.hasNext()) {
            if (!(it.next() instanceof String))
                throw new BadRequestException("Option should be string");
        }

    }

    public static void checkIfSingleQuestion(Poll poll) throws BadRequestException {
        if (poll.getQuestions().size() != 1)
            throw new BadRequestException("Not a single Question Poll");
    }

    public static void checkOptionQuestionPollSame(String pollId,
                                                   Long questionId,
                                                   Option option) throws BadRequestException {
        if (option.getQuestion().getQuestionId() != questionId)
            throw new BadRequestException("Option doesnot belong to questionId");
        if (!(option.getQuestion().getPoll().getPollId().equals(pollId)))
            throw new BadRequestException("Option doesnot belong to Poll");

    }


}
