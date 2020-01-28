package com.pollapp.validator;

import com.pollapp.dto.*;
import com.pollapp.exception.BadRequestException;

import javax.management.relation.InvalidRelationTypeException;
import java.util.Iterator;

public class PollValidator {

    public static void validatePollToken(String token) throws BadRequestException {
        if(token.length() != 36)
            throw new BadRequestException("Invalid Token");
    }

    public static void validatePoll(PollDetailDTO pollDetailDTO) throws  BadRequestException {
        PollValidator.validatePollToken(pollDetailDTO.getToken());

        if(pollDetailDTO.getQuestions().isEmpty())
            throw new BadRequestException("No Questions in Poll");
    }

    public  static void validateQuestion(QuestionDTO questionDTO) throws BadRequestException {
        if(questionDTO.getQuestion().length()==0)
            throw new BadRequestException("Question is Blank");
        if(questionDTO.getOptions().size()<2)
            throw new BadRequestException("Minimum 2 Options needed");

    }

    public static void validateOption(OptionDTO optionDTO) throws BadRequestException {
        if(optionDTO.getOption().length()==0)
            throw new BadRequestException("Option is blank");
    }

    public static void validateVote(VoteDetailDTO voteDetailDTO) throws BadRequestException {
        PollValidator.validatePollToken(voteDetailDTO.getPollId());

        if(voteDetailDTO.getQuestionId()<=0)
            throw new BadRequestException("Invalid QuestionId");

        if(voteDetailDTO.getOptionId()<=0)
            throw new BadRequestException("Invalid OptionId");

    }

    public static void validateQuestionId(Long questionId) throws BadRequestException {
        if(questionId<=0)
            throw new BadRequestException("Invalid QuestionId");
    }

    public static void validateOptionId(Long optionId) throws BadRequestException {
        if(optionId<=0)
            throw new BadRequestException("Invalid OptionId");
    }

    public  static void  singleQuesPollValidator(SingleQuesPollDTO singleQuesPollDTO) throws BadRequestException {

        PollValidator.validatePollToken(singleQuesPollDTO.getToken());
        if (singleQuesPollDTO.getQuestion().length()==0)
            throw new BadRequestException("Question is Blank");
        if(singleQuesPollDTO.getOptions().size()<2)
            throw new BadRequestException("Minimum 2 optins required");
        Iterator it = singleQuesPollDTO.getOptions().iterator();
        while (it.hasNext()) {
            if(!(it.next() instanceof String))
                throw new BadRequestException("Option should be string");
        }

    }


}
