package a4.KU_TY_backend.KU_TY_backend.entity;


import a4.KU_TY_backend.KU_TY_backend.response.FeedbackResponse;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue
    private UUID feedbackId;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    @Column(nullable = false)
    private String feedback;

    public FeedbackResponse toResponse(){
        FeedbackResponse dto = new FeedbackResponse();
        dto.setFeedback(feedback);
        dto.setEventId(event.getEventId());
        dto.setUserId(user == null ? null : user.getUserId());
        return dto;
    }

}
