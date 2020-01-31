package com.pollapp.controller;

import com.pollapp.dto.embed.EmbedDTO;
import com.pollapp.dto.response.OptionResponseDTO;
import com.pollapp.entity.Option;
import com.pollapp.entity.Question;
import com.pollapp.exception.BadRequestException;
import com.pollapp.service.PollService;
import com.pollapp.validator.PollValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class EmbedController {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    private PollService pollService;

    @GetMapping(value = "embed/poll/{pollId}/{quesId}")
    @CrossOrigin
    public String getQuestion(Model model, @PathVariable("pollId") String pollId, @PathVariable("quesId") Long quesId) {

        try {
            //log.info("Checkpoint 1 " );
            Question question = pollService.getQuestion(quesId);
            //log.info("Checkpoint 2 " );
            if(question==null)
                throw new BadRequestException("No question");
            //log.info("Checkpoint 3 " );
            model.addAttribute("question",question.getQuestion());

            List<OptionResponseDTO> optionResponseDTOList = new ArrayList<>();
            List<Option> options = question.getOptions();
            //log.info("Checkpoint 4 : Count" + options.size());
            Iterator it = options.iterator();
            while(it.hasNext()) {
                //log.info("Checkpoint 5 " );
                OptionResponseDTO optionResponseDTO = new OptionResponseDTO();
                Option option = (Option) it.next();
                optionResponseDTO.setOptionId(option.getOptionId());
                optionResponseDTO.setOption(option.getOption());
                optionResponseDTO.setVotes(option.getVotes());

                optionResponseDTOList.add(optionResponseDTO);
                //log.info("Checkpoint 6 " );
            }
            model.addAttribute("options",optionResponseDTOList);
            model.addAttribute("questionId",quesId);
            Long answerId ;
            model.addAttribute("embedDTO",new EmbedDTO());
            return "EmbeddedPoll.html";

        } catch(Exception e) {
            log.info(e.toString());
            return "InvalidPoll.html";
        }

    }


    @PostMapping(value = "embed/poll/{pollId}/{quesId}")
    @CrossOrigin
    public String updateVote(@ModelAttribute("embedDTO") EmbedDTO embedDTO,@PathVariable("pollId") String pollId ,@PathVariable("quesId") Long quesId) {
        try{
            log.info("Answer Id is" + Long.parseLong(embedDTO.getAnswerId()));
            log.info("Question Id is " + quesId);
            PollValidator.validateQuestionId(quesId);
            pollService.updateVote(quesId,Long.parseLong(embedDTO.getAnswerId()));
            return "ThankYou.html";

        } catch(Exception e) {
            log.info(e.toString());
            return "BackError.html";
        }

    }



}
