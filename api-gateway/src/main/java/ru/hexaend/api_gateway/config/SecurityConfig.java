package ru.hexaend.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity,
                                                            ServerOAuth2AuthorizationRequestResolver resolver,
                                                            ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository,
                                                            ServerLogoutSuccessHandler logoutSuccessHandler,
                                                            ServerLogoutHandler logoutHandler) {
        httpSecurity.authorizeExchange(
                        exchange -> exchange
                                .pathMatchers("/actuator/**", "/access-token/**", "/id-token").permitAll()
                                .anyExchange().authenticated()
                ).oauth2Login(
                        login -> login
                                .authorizationRequestResolver(resolver)
                                .authorizedClientRepository(auth2AuthorizedClientRepository)
                )
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler).logoutHandler(logoutHandler));


        return httpSecurity.build();
    }

    @Bean
    ServerOAuth2AuthorizationRequestResolver requestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }

    @Bean
    ServerLogoutSuccessHandler logoutHandler(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(reactiveClientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/test");
        return oidcLogoutSuccessHandler;
    }

    @Bean
    ServerLogoutHandler myLogoutHandler() {
        return new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(),
                new WebSessionServerLogoutHandler(),
                new HeaderWriterServerLogoutHandler((
                        new ClearSiteDataServerHttpHeadersWriter(ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES)
                ))
        );
    }

}

@RestController
class AuthInfoController {

    private static final Logger log = LoggerFactory.getLogger(AuthInfoController.class);

    public OAuth2AccessToken getAccessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        return client.getAccessToken();
    }

    @GetMapping("/id-token")
    public OidcIdToken getIdToken(@AuthenticationPrincipal OidcUser oidcUser) {
        return oidcUser.getIdToken();
    }
}
