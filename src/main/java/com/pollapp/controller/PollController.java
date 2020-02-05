package com.pollapp.controller;

import com.pollapp.dto.request.MultiVoteDetailDTO;
import com.pollapp.dto.request.PollDetailDTO;
import com.pollapp.dto.request.SingleQuesPollDTO;
import com.pollapp.dto.response.PollResponseDTO;
import com.pollapp.dto.response.PollResponseMiniDTO;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class PollController {

    private static final Logger log = Logger.getLogger(PollController.class.getName());

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    //CREATE POLL WITH MULTIPLE QUESTIONS
    @PostMapping(value = "/poll")
    @CrossOrigin
    public ResponseEntity<Object> createPoll(@RequestBody PollDetailDTO pollDetailDTO) {
        try {
            User user = userService.getUserByToken(pollDetailDTO.getToken());
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            String token = pollService.createPoll(user, pollDetailDTO);
            if (token == null) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    //INITIAL METHOD FOR FETCHING POLL DETAILS
    @GetMapping(value = "/poll/{id}")
    @CrossOrigin
    public ResponseEntity<Object> getPoll(@PathVariable("id") String pollId) {
        try {
            PollValidator.validatePollToken(pollId);
            Poll poll = pollService.getPoll(pollId);
            if (poll == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            //FOR FETCHING SINGLE QUESTION POLLS
            //PollValidator.checkIfSingleQuestion(poll);
            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    //INITIAL METHOD FOR VOTING
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
            boolean updated = pollService.updateVote(questionId, optionId);
            if (!updated)
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

            Poll poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }


    }

    //******************************************************************************************************************
    //************************************ MULTI QUESTION POLL VOTING **************************************************

    //VOTING FOR POLL WITH MULTIPLE QUESTIONS
    @PutMapping(value = "/poll2/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> giveMultiVote(@PathVariable("pollId") String pollId,
                                                @RequestBody MultiVoteDetailDTO multiVoteDetailDTO,
                                                @RequestParam("token") String userToken) {
        try {
            if (multiVoteDetailDTO.getQuestionIds().size() != multiVoteDetailDTO.getOptionIds().size())
                throw new BadRequestException("Size of question and option not equal");

            PollValidator.validatePollToken(pollId);
            UserValidator.validateUserToken(userToken);
            log.info(userToken);

            int arrSize = multiVoteDetailDTO.getOptionIds().size();
            for (int i = 0; i < arrSize; i++) {
                log.info(multiVoteDetailDTO.getQuestionIds().get(i) + " : " + multiVoteDetailDTO.getOptionIds().get(i));
            }
            //return new ResponseEntity<>(multiVoteDetailDTO,HttpStatus.OK);

            PollResponseDTO pollResponseDTO;
            boolean updated;
            Poll poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            boolean isPrivate = poll.isPrivate();

            if (isPrivate) {

                //throw new BadRequestException("Only public multi voting allowed");

                User user = userService.getUserByToken(userToken);
                if(user==null)
                    return new ResponseEntity<>("Invalid User Token",HttpStatus.UNAUTHORIZED);

                if(pollService.hasPrivateVoted(poll,user.getId()))
                    return new ResponseEntity<>("Already Voted for this Poll",HttpStatus.FORBIDDEN);

                if(user.getId()==poll.getUser().getId())
                    throw new BadRequestException("Creator cannot Vote");

                updated = pollService.updateMultiVote(true,user, poll, multiVoteDetailDTO.getQuestionIds(), multiVoteDetailDTO.getOptionIds());


            } else {

                boolean hasVoted = pollService.hasPublicTokenVoted(poll, userToken);
                if (hasVoted)
                    return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

                updated = pollService.updateMultiVote(false,userToken, poll, multiVoteDetailDTO.getQuestionIds(), multiVoteDetailDTO.getOptionIds());

            }
            poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    //******************************************************************************************************************
    //******************************* SINGLE QUESTION POLL CREATION ****************************************************

    //MAKE POLL WITH SINGLE QUESTION
    @PostMapping(value = "/poll/singleques")
    @CrossOrigin
    public ResponseEntity<Object> createSinglePoll(@RequestBody SingleQuesPollDTO singleQuesPollDTO) {
        try {

            UserValidator.validateUserToken(singleQuesPollDTO.getToken());

            if (singleQuesPollDTO.isPrivate())
                log.info("true");
            else
                log.info("false");
            log.info("Creation Checkpoint 1");
            //log.info(singleQuesPollDTO.getToken());
            User user = userService.getUserByToken(singleQuesPollDTO.getToken());
            if (user == null)
                return new ResponseEntity<>("No User Present", HttpStatus.UNAUTHORIZED);

            log.info("Creation Checkpoint 2");
            String token = pollService.createSinglePoll(user, singleQuesPollDTO);
            log.info("Creation Checkpoint 3");
            if (token == null)
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            log.info("Creation Checkpoint 4");
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Creation Checkpoint 5");
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    //******************************************************************************************************************
    //************************************ STORE PRIVATE RESPONSES *****************************************************

    //GET POLL DETAILS
    @GetMapping(value = "/validatedpoll/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> getValidatedPoll(@RequestParam(name = "token", required = false) String userToken,
                                                   @PathVariable("pollId") String pollId) {
        try {
            PollValidator.validatePollToken(pollId);
            Poll poll = pollService.getPoll(pollId);
            ResponseEntity responseEntity;
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            boolean isPrivate = poll.isPrivate();
            if (isPrivate) {
                if (userToken == null)
                    return new ResponseEntity<>("No userToken Provided for private poll", HttpStatus.UNAUTHORIZED);
                UserValidator.validateUserToken(userToken);
                User user = userService.getUserByToken(userToken);
                if (user == null)
                    return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

            }

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
            responseEntity = new ResponseEntity(pollResponseDTO, HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.valueOf(403));
        }
    }

    //VOTE
    @PutMapping(value = "/validatedpoll/{pollId}/{questionId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> giveValidatedVote(@RequestParam(name = "token", required = false) String userToken,
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
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            boolean isPrivate = poll.isPrivate();

            if (isPrivate) {
                if (userToken == null)
                    return new ResponseEntity<>("No userToken Provided for private poll", HttpStatus.UNAUTHORIZED);
                UserValidator.validateUserToken(userToken);
                User user = userService.getUserByToken(userToken);
                if (user == null)
                    return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

                if (user.getId() == poll.getUser().getId())
                    throw new BadRequestException("Creator cannot Vote");

                updated = pollService.updateValidatedVote(user, pollId, questionId, optionId);

            } else {
                updated = pollService.updateVote(questionId, optionId);

            }

            poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    //******************************************************************************************************************
    //********************************* PUBLIC AND PRIVATE TOKENS ******************************************************

    //GET POLL DETAILS
    @GetMapping(value = "/validatedbothpoll/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> getValidatedBothPoll(@RequestParam(name = "token", required = true) String userToken,
                                                       @PathVariable("pollId") String pollId) {
        try {
            PollValidator.validatePollToken(pollId);
            UserValidator.validateUserToken(userToken);

            Poll poll = pollService.getPoll(pollId);
            ResponseEntity responseEntity;
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            boolean isPrivate = poll.isPrivate();
            if (isPrivate) {

                User user = userService.getUserByToken(userToken);
                if (user == null)
                    return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);


                if (pollService.hasPrivateVoted(poll, user.getId()))
                    return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

                PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
                responseEntity = new ResponseEntity(pollResponseDTO, HttpStatus.OK);

            } else {

                boolean hasVoted = pollService.hasPublicTokenVoted(poll, userToken);

                if (hasVoted)
                    return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

                PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
                responseEntity = new ResponseEntity(pollResponseDTO, HttpStatus.OK);
            }
            return responseEntity;

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.valueOf(403));
        }
    }

    //VOTE
    @PutMapping(value = "/validatedbothpoll/{pollId}/{questionId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> giveValidatedBothVote(@RequestParam(name = "token", required = true) String userToken,
                                                        @PathVariable("pollId") String pollId,
                                                        @PathVariable("questionId") Long questionId,
                                                        @PathVariable("optionId") Long optionId) {

        try {
            PollValidator.validatePollToken(pollId);
            PollValidator.validateQuestionId(questionId);
            PollValidator.validateOptionId(optionId);
            UserValidator.validateUserToken(userToken);

            PollResponseDTO pollResponseDTO;
            boolean updated;
            Poll poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            boolean isPrivate = poll.isPrivate();

            if (isPrivate) {

                User user = userService.getUserByToken(userToken);
                if (user == null)
                    return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

                if (pollService.hasPrivateVoted(poll, user.getId()))
                    return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

                if (user.getId() == poll.getUser().getId())
                    throw new BadRequestException("Creator cannot Vote");

                updated = pollService.updateValidatedVote(user, pollId, questionId, optionId);

            } else {

                boolean hasVoted = pollService.hasPublicTokenVoted(poll, userToken);
                if (hasVoted)
                    return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

                updated = pollService.updatePublicValidatedVote(userToken, questionId, optionId);

            }
            poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);


        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }

    }

    //******************************************************************************************************************
    //****************************************  GET   STATUS  **********************************************************


    //GET STATUS OF POLL
    @GetMapping(value = "/poll/{pollId}/status")
    @CrossOrigin
    public ResponseEntity<Object> getStatus(@PathVariable("pollId") String pollId,
                                            @RequestParam(name = "token", required = true) String userToken) {

        try {
            UserValidator.validateUserToken(userToken);
            PollValidator.validatePollToken(pollId);

            User user = userService.getUserByToken(userToken);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

            Poll poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>("No Poll by this PollID", HttpStatus.NO_CONTENT);

            if (poll.getUser().getId() != user.getId())
                return new ResponseEntity<>("Poll Belongs to Other Account", HttpStatus.UNAUTHORIZED);

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, true);

            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);


        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);

        }
    }

    //******************************************************************************************************************
    //************************************** EMBEDDED PART *************************************************************

    //GET CREATED POLLS
    @GetMapping(value = "polls")
    @CrossOrigin
    public ResponseEntity<Object> getPolls(@RequestParam("token") String userToken) {
        try {
            UserValidator.validateUserToken(userToken);
            User user = userService.getUserByToken(userToken);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

            //List<Poll> pollList = user.getPolls();
            List<Poll> pollList = pollService.getUserPolls(user.getId());
            List<PollResponseDTO> pollResponseDTOList = new ArrayList<>();
            Iterator it = pollList.iterator();
            while(it.hasNext()) {
                pollResponseDTOList.add(new PollResponseDTO((Poll) it.next(),false));
            }

            return new ResponseEntity<>(pollResponseDTOList,HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }

    //GET LIST OF POLL IDS AND PRIVATE/PUBLIC
    @GetMapping(value = "polllist")
    @CrossOrigin
    public ResponseEntity<Object> getPollList(@RequestParam("token") String userToken) {
        try {
            UserValidator.validateUserToken(userToken);
            User user = userService.getUserByToken(userToken);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

            //List<Poll> pollList = user.getPolls();
            List<Poll> pollList = pollService.getUserPolls(user.getId());
            List<PollResponseMiniDTO> pollResponseMiniDTOList = new ArrayList<>();
            Iterator it = pollList.iterator();
            while(it.hasNext()) {
                pollResponseMiniDTOList.add(new PollResponseMiniDTO((Poll) it.next()));
            }

            return new ResponseEntity<>(pollResponseMiniDTOList,HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(),HttpStatus.BAD_REQUEST);
        }

    }


    //SETTING EMBEDDED POLL
    @PutMapping(value = "embed/poll/{pollId}")
    @CrossOrigin
    public ResponseEntity<Object> setEmbedding(@PathVariable("pollId") String pollId,
                                               @RequestParam("token") String userToken) {
        try {
            PollValidator.validatePollToken(pollId);
            UserValidator.validateUserToken(userToken);

            User user = userService.getUserByToken(userToken);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.UNAUTHORIZED);

            Poll poll = pollService.getPoll(pollId);
            if (poll == null)
                return new ResponseEntity<>("No Poll by this PollID", HttpStatus.NO_CONTENT);

            if (poll.isPrivate())
                return new ResponseEntity<>("Only Public Polls Can be Embedded ", HttpStatus.BAD_REQUEST);

            pollService.setEmbeddedPoll(user, poll);
            return new ResponseEntity<>("Success", HttpStatus.OK);


        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);

        }
    }

    //GETTING EMBEDDED POLL DETAILS
    @GetMapping(value = "/embed/{userId}/poll")
    @CrossOrigin
    public ResponseEntity<Object> getEmbedding(@PathVariable("userId") Long userId,
                                               @RequestParam("token") String userToken) {
        try {
            if (userId < 1)
                throw new BadRequestException("Invalid User Id");

            User user = userService.getUserById(userId);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.NOT_FOUND);

            Poll poll = user.getEmbeddedPoll();
            if (poll == null)
                return new ResponseEntity<>("No Embedding Has Been Set", HttpStatus.NOT_FOUND);

            boolean hasVoted = pollService.hasPublicTokenVoted(poll, userToken);
            if (hasVoted)
                return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

            PollResponseDTO pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity(pollResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    //VOTING
    @PutMapping(value = "/embed/{userId}/poll/{quesId}/{optionId}")
    @CrossOrigin
    public ResponseEntity<Object> voteEmbedding(@PathVariable("userId") Long userId,
                                                @PathVariable("quesId") Long questionId,
                                                @PathVariable("optionId") Long optionId,
                                                @RequestParam(name = "token", required = true) String userToken) {
        try {

            PollValidator.validateQuestionId(questionId);
            PollValidator.validateOptionId(optionId);
            UserValidator.validateUserToken(userToken);
            if (userId < 1)
                throw new BadRequestException("Invalid User Id");

            User user = userService.getUserById(userId);
            if (user == null)
                return new ResponseEntity<>("Invalid User Token", HttpStatus.NOT_FOUND);

            Poll poll = user.getEmbeddedPoll();
            if (poll == null)
                return new ResponseEntity<>("No Embedding Has Been Set", HttpStatus.NOT_FOUND);

            boolean isPrivate = poll.isPrivate();
            if (isPrivate)
                return new ResponseEntity<>("Only Public Polls Can be Voted ", HttpStatus.BAD_REQUEST);

            boolean hasVoted = pollService.hasPublicTokenVoted(poll, userToken);
            if (hasVoted)
                return new ResponseEntity<>("Already Voted for this Poll", HttpStatus.FORBIDDEN);

            boolean updated;
            updated = pollService.updatePublicValidatedVote(userToken, questionId, optionId);

            poll = pollService.getPoll(poll.getPollId());
            if (poll == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            PollResponseDTO pollResponseDTO;
            pollResponseDTO = new PollResponseDTO(poll, false);
            return new ResponseEntity<>(pollResponseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }


}
