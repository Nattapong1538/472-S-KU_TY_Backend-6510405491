package a4.KU_TY_backend.KU_TY_backend.service;

import a4.KU_TY_backend.KU_TY_backend.entity.*;
import a4.KU_TY_backend.KU_TY_backend.exception.ConflictException;
import a4.KU_TY_backend.KU_TY_backend.exception.NotFoundException;
import a4.KU_TY_backend.KU_TY_backend.exception.SystemException;
import a4.KU_TY_backend.KU_TY_backend.repository.EventRepository;
import a4.KU_TY_backend.KU_TY_backend.repository.EventUserRepository;
import a4.KU_TY_backend.KU_TY_backend.repository.UserRepository;
import a4.KU_TY_backend.KU_TY_backend.request.*;
import a4.KU_TY_backend.KU_TY_backend.response.EventResponse;
import a4.KU_TY_backend.KU_TY_backend.response.UserResponse;
import a4.KU_TY_backend.KU_TY_backend.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventUserRepository eventUserRepository;
    @Autowired
    private Validator validator;
    public List<UserResponse> getAllUser(){
        return userRepository.findAll().stream().map(User::toResponse).collect(Collectors.toList());
    }
    public UserResponse getUserById(UUID userId){
        validator.userIdValidate(userId);
        User user = userRepository.findById(userId).get();
        return user.toResponse();
    }
    public UserResponse getUserByUsername(String username){
        if(username == null) throw new SystemException("username must not be null");
        User user = userRepository.findByUsername(username);
        if(user == null) throw new NotFoundException("User not found");
        return user.toResponse();
    }
    public UserResponse updateDescription(UpdateUserDescriptionRequest request){
        validator.userIdValidate(request.getUserId());
        User user = userRepository.findById(request.getUserId()).get();
        user.setDescription(request.getDescription());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user).toResponse();
    }
    public UserResponse updateEmail(UpdateUserEmailRequest request){
        validator.userIdValidate(request.getUserId());
        User user = userRepository.findById(request.getUserId()).get();
        user.setEmail(request.getEmail());
        return userRepository.save(user).toResponse();
    }
    public UserResponse updateName(UpdateUserNameRequest request){
        validator.userIdValidate(request.getUserId());
        User user = userRepository.findById(request.getUserId()).get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user).toResponse();
    }
    public EventResponse joinEvent(JoinEventRequest request){
        UUID eventId = request.getEventId();
        UUID userId = request.getUserId();

        if(eventId == null || userId == null) throw new NotFoundException("User id and event id must not be null");
        Event event =  eventRepository.findById(eventId).orElseThrow(()-> new NotFoundException("Event not found"));
        User user =  userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));

        EventUserKey eventUserKey = new EventUserKey(eventId, userId);
        if(eventUserRepository.findById(eventUserKey).isPresent()) throw new ConflictException("User has already joined this event");

        event.setAttendeeCount(event.getAttendeeCount() + 1);

        EventUser eventUser = new EventUser();
        eventUser.setKey(eventUserKey);
        eventUser.setEvent(event);
        eventUser.setUser(user);

        eventUserRepository.save(eventUser);

        return event.toResponse();
    }
    public List<EventResponse> getAllJoinedEvent(UUID userId){
        validator.userIdValidate(userId);
        User user = userRepository.findById(userId).get();
        return user.getJoinedEventList().stream().map(EventUser::getEvent).map(Event::toResponse).collect(Collectors.toList());
    }
    public UserResponse updateUser(UpdateUserRequest request){
        UUID userId = request.getUserId();
        validator.userIdValidate(userId);
        User user = userRepository.findById(userId).get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDescription(request.getDescription());
        return userRepository.save(user).toResponse();
    }
    public List<EventResponse> getAllCreatedEvent(UUID userId){
        validator.userIdValidate(userId);
        User user = userRepository.findById(userId).get();
        return user.getCreatedEventList().stream().map(Event::toResponse).collect(Collectors.toList());
    }
    public void quitEvent(QuitEventRequest request){
        UUID userId = request.getUserId();
        UUID eventId = request.getEventId();
        validator.userIdValidate(userId);
        validator.eventIdValidate(eventId);
        Event event = eventRepository.findById(eventId).get();
        EventUser eventUser = eventUserRepository.findById(new EventUserKey(eventId, userId)).orElseThrow(()-> new NotFoundException("User not in event"));
        eventUserRepository.delete(eventUser);
        event.setAttendeeCount(event.getAttendeeCount() - 1);
        eventRepository.save(event);
    }
    public void kickFromEvent(KickFromEventRequest request){
        UUID ownerId = request.getOwnerId();
        UUID eventId = request.getEventId();
        UUID participantId = request.getParticipantId();
        validator.userIdValidate(ownerId);
        validator.userIdValidate(participantId);
        validator.eventIdValidate(eventId);
        Event event = eventRepository.findById(eventId).get();
        User owner = userRepository.findById(ownerId).get();
        if(!event.getCreatedBy().equals(owner)) throw new NotFoundException("You are not owner");
        EventUser eventUser = eventUserRepository.findById(new EventUserKey(eventId, participantId)).orElseThrow(()-> new NotFoundException("User not in event"));
        eventUserRepository.delete(eventUser);
        event.setAttendeeCount(event.getAttendeeCount() - 1);
        eventRepository.save(event);
    }
    public UserResponse updateImage(UUID userId, String imageUrl){
        validator.userIdValidate(userId);
        User user = userRepository.findById(userId).get();
        user.setImageUrl(imageUrl);
        return userRepository.save(user).toResponse();
    }


}
