package ru.hexaend.userservice.service;

import org.springframework.stereotype.Service;

public interface UserService {

    String getUserIdByUsername(String username);

    String getUsernameByUserId(String userId);

    String getUserIdByEmail(String email);

    String getEmailByUserId(String userId);

    String getUsernameByEmail(String email);

    String getEmailByUsername(String username);

}
