package ru.hexaend.chatservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(Customizer.withDefaults());
                })
                .cors(
                        cors -> cors.configurationSource(request -> {
                            var config = new org.springframework.web.cors.CorsConfiguration();
                            config.addAllowedOrigin("http://localhost:5173/");
                            config.addAllowedMethod("*");
                            config.addAllowedHeader("*");
                            config.setAllowCredentials(true);
                            return config;
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Client(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("index.html", "/actuator/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/chat/**").permitAll()
                        .anyRequest().authenticated());

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("spring_roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
