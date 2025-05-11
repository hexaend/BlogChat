package ru.hexaend.userservice;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

    private final Keycloak keycloak;

    public UserServiceApplication(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
