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
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
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
class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal OidcUser oidcUser) {
        log.info("OIDC User: {}", oidcUser);

        Map<String, Object> claims = oidcUser.getClaims();

        Object rolesObj = claims.get("spring_roles");
        List<GrantedAuthority> mappedAuthorities = rolesObj instanceof List<?> list
                ? list.stream()
                .map(Object::toString)
                .filter(a -> a.startsWith("ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : List.of();

        List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
        authorities.addAll(mappedAuthorities);

        var roles = authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .toList();
        return "Roles: " + roles;
    }
}
