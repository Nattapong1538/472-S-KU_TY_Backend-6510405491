package a4.KU_TY_backend.KU_TY_backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSetter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiveFeedbackRequest {
    private UUID eventId;
    private String feedback;
    private UUID userId;
    @JsonSetter("userId")
    public void setUserId(String userId) {
        this.userId = (userId == null || "null".equals(userId)) ? null : UUID.fromString(userId);
    }
}
