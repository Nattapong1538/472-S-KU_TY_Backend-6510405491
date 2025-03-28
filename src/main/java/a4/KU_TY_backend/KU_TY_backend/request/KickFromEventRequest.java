package a4.KU_TY_backend.KU_TY_backend.request;

import lombok.Data;

import java.util.UUID;

@Data
public class KickFromEventRequest {
    private UUID ownerId;
    private UUID eventId;
    private UUID participantId;
}
