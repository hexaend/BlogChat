package ru.hexaend.userservice.service.impl;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.stereotype.Service;
import ru.hexaend.userservice.config.KeycloakConfig;
import ru.hexaend.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final KeycloakConfig keycloakConfig;
    private final Keycloak keycloak;
    private final EnvironmentManager environmentManager;

    public UserServiceImpl(KeycloakConfig keycloakConfig, Keycloak keycloak, EnvironmentManager environmentManager) {
        this.keycloakConfig = keycloakConfig;
        this.keycloak = keycloak;
        this.environmentManager = environmentManager;
    }

    @Override
    public String getUserIdByUsername(String username) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getUsername().equals(username))
                .findFirst()
                .map(AbstractUserRepresentation::getId)
                .orElse(null);
    }

    @Override
    public String getUsernameByUserId(String userId) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getId().equals(userId))
                .findFirst()
                .map(AbstractUserRepresentation::getUsername)
                .orElse(null);
    }

    @Override
    public String getUserIdByEmail(String email) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getEmail().equals(email))
                .findFirst()
                .map(AbstractUserRepresentation::getId)
                .orElse(null);
    }

    @Override
    public String getEmailByUserId(String userId) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getId().equals(userId))
                .findFirst()
                .map(AbstractUserRepresentation::getEmail)
                .orElse(null);
    }

    @Override
    public String getUsernameByEmail(String email) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getEmail().equals(email))
                .findFirst()
                .map(AbstractUserRepresentation::getUsername)
                .orElse(null);
    }

    @Override
    public String getEmailByUsername(String username) {
        return keycloak.realm(keycloakConfig.getRealm()).users().list().stream()
                .filter(userRepresentation -> userRepresentation.getUsername().equals(username))
                .findFirst()
                .map(AbstractUserRepresentation::getEmail)
                .orElse(null);
    }
}
