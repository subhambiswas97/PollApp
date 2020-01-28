package com.pollapp.controller;

import com.pollapp.dto.*;
import com.pollapp.entity.Poll;
import com.pollapp.entity.User;
import com.pollapp.service.PollService;
import com.pollapp.service.UserService;
import com.pollapp.validator.PollValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class PollController {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/poll")
    @CrossOrigin
    public ResponseEntity<Object> createPoll(@RequestBody PollDetailDTO pollDetailDTO) {
        try {
            User user = userService.getUserByToken(pollDetailDTO.getToken());
            if (user==null)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            String token = pollService.createPoll(user,pollDetailDTO);
            if (token == null)
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/poll/{id}")
    @CrossOrigin
    public ResponseEntity<Object> getPoll(@PathVariable("id") String pollId) {
        try {
            PollValidator.validatePollToken(pollId);
            Poll poll = pollService.getPoll(pollId);
            if(poll == null)
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
            return new ResponseEntity<>(pollResponseDTO,HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping(value = "/poll/{pollId}/{questionId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> giveVote(@PathVariable("pollId") String pollId, @PathVariable("questionId") Long questionId, @PathVariable("optionId") Long optionId) {

        try {
            //PollValidator.validateVote(voteDetailDTO);
            PollValidator.validatePollToken(pollId);
            PollValidator.validateQuestionId(questionId);
            PollValidator.validateOptionId(optionId);
            boolean updated = pollService.updateVote(questionId,optionId);
            if(!updated)
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

            Poll poll = pollService.getPoll(pollId);
            if(poll == null)
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
            return new ResponseEntity<>(pollResponseDTO,HttpStatus.OK);

        } catch(Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }


    }

    @PutMapping(value = "/poll2/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> giveMultiVote(@PathVariable("pollId") String pollId, @RequestBody MultiVoteDetailDTO multiVoteDetailDTO) {
        return new ResponseEntity<>(multiVoteDetailDTO,HttpStatus.OK);
    }

    @PostMapping(value = "/poll/singleques")
    @CrossOrigin
    public ResponseEntity<Object> createSinglePoll(@RequestBody SingleQuesPollDTO singleQuesPollDTO) {
        try {
            User user = userService.getUserByToken(singleQuesPollDTO.getToken());
            if (user==null)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            String token = pollService.createSinglePoll(user,singleQuesPollDTO);
            if (token == null)
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }
    }

}
