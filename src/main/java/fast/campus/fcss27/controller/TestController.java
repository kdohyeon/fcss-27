package fast.campus.fcss27.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RegisteredClientRepository repository;

    @GetMapping("/api/v1/test")
    public String findClient() {
        return repository.findByClientId("articles-client").getClientId();
    }
}
