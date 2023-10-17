package uk.co.essarsoftware.par.engine.core.app;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class InfoController
{

    @GetMapping(path = "/health")
    public Mono<Map<String, String>> getHealth() {

        return Mono.just(Map.of("status", "OK"));
        
    }
    
    @GetMapping(path = "/version")
    public Mono<Map<String, String>> getVersion() {

        // Look for version in package
        String implVersion = getClass().getPackage().getImplementationVersion();
        return Mono.just(Map.of("version", implVersion == null ? "1.0.0" : implVersion));
        
    }
}
