package ru.hexaend.chatservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", path="/user")
public interface FeignUserService {
    @GetMapping("/username")
    ResponseEntity<String> getUsername(@RequestParam(required = false) String id,
                                  @RequestParam(required = false) String email);

    @GetMapping("/id")
    ResponseEntity<String> getUserId(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String email);

    @GetMapping("/email")
    ResponseEntity<String> getEmail(@RequestParam(required = false) String id,
                               @RequestParam(required = false) String username);
}
