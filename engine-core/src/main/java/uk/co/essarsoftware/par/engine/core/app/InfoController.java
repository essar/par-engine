package uk.co.essarsoftware.par.engine.core.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class InfoController
{

    @GetMapping(path = "/errors/caught")
    public Mono<?> getCaughtError() {

        return Mono.error(new RuntimeException("Testing caught error"));
        
    }

    @GetMapping(path = "/errors/uncaught")
    public Mono<String> getUncaughtError() {

        throw new RuntimeException("Testing uncaught error");
        
    }
    
    @GetMapping(path = "/version")
    public Mono<String> getVersion() {

        // Look for version in package
        String implVersion = getClass().getPackage().getImplementationVersion();
        if (implVersion != null) {
            return Mono.just(implVersion);
        }

        return Mono.just("1.0.0");
        
    }
}
