package io.github.nktltvnv.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.header.ClearSiteDataServerHttpHeadersWriter;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            final ServerHttpSecurity httpSecurity,
            final ServerOAuth2AuthorizationRequestResolver resolver,
            final ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientRepository,
            final ServerLogoutSuccessHandler logoutSuccessHandler,
            final ServerLogoutHandler logoutHandler) {
        return httpSecurity
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/actuator/**", "/access-token/**", "/id-token")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationRequestResolver(resolver)
                        .authorizedClientRepository(auth2AuthorizedClientRepository))
                .logout(logout ->
                        logout.logoutSuccessHandler(logoutSuccessHandler).logoutHandler(logoutHandler))
                .csrf(Customizer.withDefaults())
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }

    @Bean
    public ServerOAuth2AuthorizationRequestResolver requestResolver(
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
        return resolver;
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler(
            final ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/mod");
        return oidcLogoutSuccessHandler;
    }

    @Bean
    public ServerLogoutHandler logoutHandler() {
        return new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(),
                new WebSessionServerLogoutHandler(),
                new HeaderWriterServerLogoutHandler(new ClearSiteDataServerHttpHeadersWriter(
                        ClearSiteDataServerHttpHeadersWriter.Directive.COOKIES)));
    }
}
