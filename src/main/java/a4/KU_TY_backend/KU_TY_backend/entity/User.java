package a4.KU_TY_backend.KU_TY_backend.entity;

import a4.KU_TY_backend.KU_TY_backend.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID userId;
    @Column(nullable = false)
    private String username;
    private String description;
    private String email;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "department_name_th", nullable = false)
    private String departmentNameTh;
    @Column(name = "major_name", nullable = false)
    private String majorName;
    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Event> createdEventList;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<EventUser> joinedEventList;
    private String imageUrl;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Feedback> givedFeedbackList;

    public UserResponse toResponse(){
        UserResponse response = new UserResponse();
        response.setDescription(description);
        response.setEmail(email);
        response.setUsername(username);
        response.setLastName(lastName);
        response.setFirstName(firstName);
        response.setUserId(userId);
        response.setMajorName(majorName);
        response.setDepartmentNameTh(departmentNameTh);
        response.setImageUrl(imageUrl);
        return response;
    }
}
