package ru.hexaend.userservice.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@ConfigurationProperties(value = "keycloak")
@Component
@Data
public class KeycloakConfig {

    private String url;
    private String realm;
    private String username;
    private String password;
    private String client_id;
    private String client_secret;

    @Bean
    public Keycloak keycloak() {
        var keycloak = KeycloakBuilder.builder()
                .serverUrl(url)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .username(username)
                .password(password)
                .clientId(client_id)
                .clientSecret(client_secret)
                .build();

        log.info("Keycloak users: {}", keycloak.realm(realm)
                .users()
                .list()
                .stream()
                .map(AbstractUserRepresentation::getUsername).toList());
        return keycloak;
    }

}
