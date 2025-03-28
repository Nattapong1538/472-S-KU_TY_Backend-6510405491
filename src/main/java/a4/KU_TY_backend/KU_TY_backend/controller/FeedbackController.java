package a4.KU_TY_backend.KU_TY_backend.controller;

import a4.KU_TY_backend.KU_TY_backend.request.GiveFeedbackRequest;
import a4.KU_TY_backend.KU_TY_backend.response.ResponseHandler;
import a4.KU_TY_backend.KU_TY_backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @PostMapping("/feedback")
    public ResponseEntity<Object> giveFeedback(@RequestBody GiveFeedbackRequest request){
        feedbackService.giveFeedback(request);
        ResponseEntity<Object> response = ResponseHandler.responseBuilder("Give feedback to event success", HttpStatus.OK, null);
        return response;
    }
    @GetMapping("/feedback/{eventId}")
    public ResponseEntity<Object> getFeedbackFromEvent(@PathVariable("eventId") UUID eventId){
        return ResponseHandler.responseBuilder("Get feedback from event success", HttpStatus.OK, feedbackService.getFeedbackFromEvent(eventId));
    }

}
