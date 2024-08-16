package fast.campus.fcss27.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

public class ClientConfig {

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
        httpSecurity.with(configurer, customizer -> configurer.registeredClientRepository(registeredClientRepository()));
        return httpSecurity.build();
    }

    private RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                registeredClient1(),
                registeredClient2()
        );
    }

    private RegisteredClient registeredClient1() {
        return RegisteredClient.withId("article-client-1")
                .build();
    }

    private RegisteredClient registeredClient2() {
        return RegisteredClient.withId("article-client-2")
                .build();
    }
}
