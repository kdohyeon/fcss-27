package fast.campus.fcss27.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()); // Enable OpenID Connect 1.0
//        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
//        http.with(configurer, customizer -> configurer.registeredClientRepository(registeredClientRepository()));
        return http.formLogin(withDefaults()).build();
    }

    private RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                registeredClient1(),
                registeredClient2()
        );
    }

    private RegisteredClient registeredClient1() {
        return RegisteredClient.withId("article-client-1")
                .clientId("article-client-1")
                .clientSecret("{noop}aaaa")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Authorization: Basic {base64 encoded String}
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // client_credentials 이용
                .clientSettings(ClientSettings.builder().build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build()) // 토큰 2시간 유효
                .build();
    }

    private RegisteredClient registeredClient2() {
        return RegisteredClient.withId("article-client-2")
                .clientId("article-client-2")
                .clientSecret("{noop}bbbb")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Authorization: Basic {base64 encoded String}
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // client_credentials 이용
                .clientSettings(ClientSettings.builder().build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build()) // 토큰 2시간 유효
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
                        .authenticated())
                .formLogin(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
                .username("admin")
                .password("password")
                .passwordEncoder(encoder::encode)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
