package a4.KU_TY_backend.KU_TY_backend.response;

import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackResponse {
    private UUID eventId;
    private UUID userId;
    private String feedback;
}
