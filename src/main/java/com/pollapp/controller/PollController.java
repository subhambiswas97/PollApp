package com.pollapp.controller;

import com.pollapp.dto.requestdto.MultiVoteDetailDTO;
import com.pollapp.dto.requestdto.PollDetailDTO;
import com.pollapp.dto.requestdto.SingleQuesPollDTO;
import com.pollapp.dto.responsedto.PollResponseDTO;
import com.pollapp.entity.Poll;
import com.pollapp.entity.User;
import com.pollapp.exception.BadRequestException;
import com.pollapp.service.PollService;
import com.pollapp.service.UserService;
import com.pollapp.validator.PollValidator;
import com.pollapp.validator.UserValidator;
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

            PollValidator.checkIfSingleQuestion(poll);
            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
            return new ResponseEntity<>(pollResponseDTO,HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping(value = "/poll/{pollId}/{questionId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> giveVote(@PathVariable("pollId") String pollId,
                                           @PathVariable("questionId") Long questionId,
                                           @PathVariable("optionId") Long optionId) {

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
            if (singleQuesPollDTO.isPrivate())
                log.info("true");
            else
                log.info("false");
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

    @GetMapping(value = "/validatedpoll/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> getValidatedPoll(@RequestParam(name = "token",required = false) String userToken,@PathVariable("pollId") String pollId) {
        try {
            PollValidator.validatePollToken(pollId);
            Poll poll = pollService.getPoll(pollId);
            ResponseEntity responseEntity;
            if(poll==null)
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

            boolean isPrivate = poll.isPrivate();
            if(isPrivate) {
                if(userToken==null)
                    return new ResponseEntity<>("No userToken Provided for private poll",HttpStatus.UNAUTHORIZED);
                UserValidator.validateUserToken(userToken);
                User user = userService.getUserByToken(userToken);
                if(user==null)
                    return new ResponseEntity<>("Invalid User Token",HttpStatus.UNAUTHORIZED);

            }

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll);
            responseEntity = new ResponseEntity(pollResponseDTO,HttpStatus.OK);

            return  responseEntity;
        } catch(Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.valueOf(403));
        }
    }

    @PutMapping(value = "/validatedpoll/{pollId}/{questionId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> giveValidatedVote(@RequestParam(name = "token",required = false) String userToken,
                                                    @PathVariable("pollId") String pollId,
                                                    @PathVariable("questionId") Long questionId,
                                                    @PathVariable("optionId") Long optionId) {

        try {
            //PollValidator.validateVote(voteDetailDTO);
            PollValidator.validatePollToken(pollId);
            PollValidator.validateQuestionId(questionId);
            PollValidator.validateOptionId(optionId);

            PollResponseDTO pollResponseDTO;
            boolean updated;
            Poll poll = pollService.getPoll(pollId);
            if(poll == null)
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);

            boolean isPrivate = poll.isPrivate();

            if(isPrivate) {
                if(userToken==null)
                    return new ResponseEntity<>("No userToken Provided for private poll",HttpStatus.UNAUTHORIZED);
                UserValidator.validateUserToken(userToken);
                User user = userService.getUserByToken(userToken);
                if(user==null)
                    return new ResponseEntity<>("Invalid User Token",HttpStatus.UNAUTHORIZED);

                updated = pollService.updateValidatedVote(user,questionId,optionId);

            } else {
                updated = pollService.updateVote(questionId,optionId);

            }

            poll = pollService.getPoll(pollId);
            if(poll == null)
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            pollResponseDTO = new PollResponseDTO(poll);
            return new ResponseEntity<>(pollResponseDTO,HttpStatus.OK);

        } catch(Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }

}
