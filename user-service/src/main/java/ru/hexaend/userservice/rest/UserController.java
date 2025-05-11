package ru.hexaend.userservice.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hexaend.userservice.service.UserService;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUsername(@RequestParam(required = false) String id,
                                         @RequestParam(required = false) String email) {
        if (id != null) {
            return ResponseEntity.ok(userService.getUsernameByUserId(id));
        } else if (email != null) {
            return ResponseEntity.ok(userService.getUsernameByEmail(email));
        } else {
            return ResponseEntity.badRequest().body("At least one parameter is required");
        }
    }

    @GetMapping("/id")
    public ResponseEntity<?> getUserId(@RequestParam(required = false) String username,
                                       @RequestParam(required = false) String email) {
        if (username != null) {
            return ResponseEntity.ok(userService.getUserIdByUsername(username));
        } else if (email != null) {
            return ResponseEntity.ok(userService.getUserIdByEmail(email));
        } else {
            return ResponseEntity.badRequest().body("At least one parameter is required");
        }
    }

    @GetMapping("/email")
    public ResponseEntity<?> getEmail(@RequestParam(required = false) String id,
                                      @RequestParam(required = false) String username) {
        if (id != null) {
            return ResponseEntity.ok(userService.getEmailByUserId(id));
        } else if (username != null) {
            return ResponseEntity.ok(userService.getEmailByUsername(username));
        } else {
            return ResponseEntity.badRequest().body("At least one parameter is required");
        }
    }

}
