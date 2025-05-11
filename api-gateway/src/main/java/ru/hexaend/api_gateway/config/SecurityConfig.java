package ru.hexaend.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
//    @Order(2)
    public SecurityWebFilterChain apiResourceServerChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        exchange -> exchange
                                .pathMatchers("/actuator/**", "/access-token/**", "/id-token", "/login", "/logout").permitAll()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyExchange().authenticated()
                )
                .cors(
                        cors -> cors
                                .configurationSource(request -> {
                                    var config = new org.springframework.web.cors.CorsConfiguration();
                                    config.addAllowedOrigin("*");
                                    config.addAllowedMethod("*");
                                    config.addAllowedHeader("*");
                                    return config;
                                })
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

//    @Bean
//    @Order(1)
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity,
                                                            ServerOAuth2AuthorizationRequestResolver resolver,
                                                            ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository,
                                                            ServerLogoutSuccessHandler logoutSuccessHandler,
                                                            ServerLogoutHandler logoutHandler) {
        httpSecurity.authorizeExchange(
                        exchange -> exchange
                                .pathMatchers("/actuator/**", "/access-token/**", "/id-token", "/login", "/logout").permitAll()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .anyExchange().authenticated()
                )
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(
                        login -> login
                                .authorizationRequestResolver(resolver)
                                .authorizedClientRepository(auth2AuthorizedClientRepository)
                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(Customizer.withDefaults())
//                )
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler).logoutHandler(logoutHandler))
        ;


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

    @GetMapping("/access-token")
    public OAuth2AccessToken getAccessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        return client.getAccessToken();
    }

    @GetMapping("/id-token")
    public OidcIdToken getIdToken(@AuthenticationPrincipal OidcUser oidcUser) {
        return oidcUser.getIdToken();
    }
}
