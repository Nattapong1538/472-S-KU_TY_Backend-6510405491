package a4.KU_TY_backend.KU_TY_backend.entity;

import a4.KU_TY_backend.KU_TY_backend.common.EventStatus;
import a4.KU_TY_backend.KU_TY_backend.response.EventResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Data
public class Event {
    @Id
    @GeneratedValue
    private UUID eventId;
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.CLOSED;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    private String location;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EventUser> joinedUserList;
    @Min(1)
    @Column(nullable = false)
    private int capacity = 1;
    @Column(name = "attendee_count", nullable = false)
    @Min(0)
    private int attendeeCount = 0;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private EventType eventType;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Feedback> feedbackList;

    public EventResponse toResponse() {
        EventResponse dto = new EventResponse();
        dto.setEventId(eventId);
        dto.setCreatedBy(createdBy);
        dto.setName(name);
        dto.setDescription(description);
        dto.setStatus(status);
        dto.setStartDate(startDate == null ? null : startDate.toLocalDate());
        dto.setStartTime(startDate == null ? null : startDate.toLocalTime());
        dto.setLocation(location);
        dto.setCapacity(capacity);
        dto.setAttendeeCount(attendeeCount);
        dto.setImageUrl(imageUrl);
        dto.setTypeName(eventType == null ? null : eventType.getTypeName());
        return dto;
    }
}
