package a4.KU_TY_backend.KU_TY_backend.controller;
import a4.KU_TY_backend.KU_TY_backend.exception.SystemException;
import a4.KU_TY_backend.KU_TY_backend.request.*;
import a4.KU_TY_backend.KU_TY_backend.response.ResponseHandler;
import a4.KU_TY_backend.KU_TY_backend.service.CloudinaryService;
import a4.KU_TY_backend.KU_TY_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping("/user")
    public ResponseEntity<Object> getAllUser(){
        return ResponseHandler.responseBuilder("Get all user success", HttpStatus.OK, userService.getAllUser());
    }
    @GetMapping("/user/userId/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") UUID userId){
        return ResponseHandler.responseBuilder("Get user success", HttpStatus.OK, userService.getUserById(userId));
    }
    @GetMapping("/user/username/{username}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable("username") String username){
        return ResponseHandler.responseBuilder("Get user success", HttpStatus.OK, userService.getUserByUsername(username));
    }
    @GetMapping("/user/userId/{userId}/joined/event")
    public ResponseEntity<Object> getAllJoinedEvent(@PathVariable UUID userId){
        return ResponseHandler.responseBuilder("Get joined event success", HttpStatus.OK, userService.getAllJoinedEvent(userId));
    }
    @GetMapping("/user/userId/{userId}/created/event")
    public ResponseEntity<Object> getAllCreatedEvent(@PathVariable UUID userId){
        return ResponseHandler.responseBuilder("Get created event success", HttpStatus.OK, userService.getAllCreatedEvent(userId));
    }
    @PutMapping("/user/update/description")
    public ResponseEntity<Object> updateUserDescription(@RequestBody UpdateUserDescriptionRequest request){
        return ResponseHandler.responseBuilder("Update user description success", HttpStatus.OK, userService.updateDescription(request));
    }
    @PutMapping("/user/update/email")
    public ResponseEntity<Object> updateUserEmail(@RequestBody UpdateUserEmailRequest request){
        return ResponseHandler.responseBuilder("Update user email success", HttpStatus.OK, userService.updateEmail(request));
    }
    @PutMapping("/user/update/name")
    public ResponseEntity<Object> updateUserName(@RequestBody UpdateUserNameRequest request){
        return ResponseHandler.responseBuilder("Update user name success", HttpStatus.OK, userService.updateName(request));
    }
    @PutMapping("/user")
    public ResponseEntity<Object> updateUser(@RequestBody UpdateUserRequest request){
        return ResponseHandler.responseBuilder("Update user success", HttpStatus.OK, userService.updateUser(request));
    }
    @PutMapping("/user/{userId}/image")
    public ResponseEntity<Object> editImageUrl(@PathVariable UUID userId, @RequestParam("image") MultipartFile imageFile){
        try {
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            return ResponseHandler.responseBuilder("Edit profile image success", HttpStatus.OK, userService.updateImage(userId, imageUrl));
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }
    @DeleteMapping("/user/quit/event")
    public ResponseEntity<Object> quitEvent(@RequestBody QuitEventRequest request){
        userService.quitEvent(request);
        return ResponseHandler.responseBuilder("Quit event success", HttpStatus.OK,null);
    }
    @DeleteMapping("/user/kick")
    public ResponseEntity<Object> kickFromEvent(@RequestBody KickFromEventRequest request){
        userService.kickFromEvent(request);
        return ResponseHandler.responseBuilder("Kick from event success", HttpStatus.OK,null);
    }
    @PostMapping("/user/join/event")
    public ResponseEntity<Object> joinEvent(@RequestBody JoinEventRequest request){
        ResponseEntity<Object> response= ResponseHandler.responseBuilder("Join event success", HttpStatus.OK, userService.joinEvent(request));
        return  response;
    }

}

